package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.bot.rag.handler.DbHandler;
import com.dahuaboke.mpda.bot.rag.handler.RateBondCalHandler;
import com.dahuaboke.mpda.bot.rag.service.DocumentDeleteService;
import com.dahuaboke.mpda.bot.rag.service.DocumentInsertService;
import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.rag.service.ProductSummaryQueryService;
import com.dahuaboke.mpda.bot.rag.utils.FundClassifierUtil;
import com.dahuaboke.mpda.bot.tools.ContentManageTool;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageResponse;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import com.dahuaboke.mpda.bot.tools.enums.FundInfoType;
import com.dahuaboke.mpda.bot.tools.enums.IsBondFund;
import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.rag.reader.DocumentReader;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RagSearchTask {

    private static final Logger log = LoggerFactory.getLogger(RagSearchTask.class);

    @Autowired
    DbHandler dbHandler;

    @Autowired
    DocumentDeleteService documentDeleteService;

    @Autowired
    DocumentInsertService documentInsertService;

    @Autowired
    ProductReportQueryService productReportQueryService;

    @Autowired
    ProductSummaryQueryService productSummaryQueryService;

    @Autowired
    ProcessingMonitor processingMonitor;

    @Autowired
    private DocumentReader documentReader;

    @Autowired
    private RateBondCalHandler rateBondCalHandler;

    @Autowired
    private ContentManageTool contentManageTool;

    private static final Long TIME_OUT = 60 * 60L;

    private static final Double MIN_NVAL = 5000000000.0;

    @Scheduled(cron = "0 0 04 * * ?")
    public void ragSearchJob() {
        log.info("开始执行pdf数据处理任务.......");

        // 获取未处理文件
        List<BrProduct> brProducts = dbHandler.markAndSelectUnprocessed(1000);
        if (brProducts.isEmpty()) {
            log.info("基金报告表不存在数据返回.......");
            return;
        }
        processDataList(brProducts);
    }

    public void processDataList(List<BrProduct> brProducts) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                10,
                15,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5000),
                new ThreadFactoryBuilder().setNameFormat("pdf-parse-%d").build()
        );

        try {
            // 分批
            List<List<BrProduct>> batches = splitIntoBatches(brProducts, 10);

            // 线程池调度
            for (List<BrProduct> batch : batches) {
                executorService.submit(() -> {
                    try {
                        ProcessingMonitor.ProcessingResult<BrProduct> result = processingMonitor.processBatch(
                                batch,
                                this::process,
                                this::failProcess,
                                BrProduct::getFundCode,
                                "PDF文件解析处理"
                        );
                        Map<BrProduct, String> failures = result.getFailures();
                        Map<String, String> collect = failures.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getFundCode(), e -> {
                            String value = e.getValue();
                            if (StringUtils.isNotEmpty(value)) {
                                return value;
                            }
                            return "未知异常";
                        }));
                        processingMonitor.writeFailuresToFile(collect, "pdf_parse_processing");
                    } catch (Exception e) {
                        log.error("子线程{}处理批次异常", Thread.currentThread().getName(), e);
                    }
                });
            }
        } catch (Exception e) {
            throw new MpdaRuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }


    public boolean process(BrProduct brProduct) {
        //设置开始时间和超时阈值
        LocalDateTime now = LocalDateTime.now();
        brProduct.setDealBgnTime(now);
        brProduct.setTmoutTimeNum(TIME_OUT);
        dbHandler.updateProduct(brProduct);

        //1. 从内管下载文件到本地
        ContentManageResponse contentManageResponse = contentManageTool.downloadFilesTool(brProduct.getInmngPlatfFileIndexNo(), brProduct.getPageCode());
        if (!contentManageResponse.isDownloadSuccess()) {
            throw new MpdaRuntimeException("内管文件下载失败");
        }
        String localFilePath = contentManageResponse.getLocalFilePath();
        Resource fileResource = getFileResource(localFilePath);
        if (fileResource == null) {
            throw new MpdaIllegalArgumentException("文件资源获取为空");
        }
        //2. 解析下载后的文件，存入向量库
        if (!insertVectorStore(brProduct, fileResource)) {
            throw new MpdaRuntimeException("插入向量库失败");
        }
        //3. 提取文件内容到对象,插入pq
        Object obj = insertPqStore(brProduct);

        //4. 计算利率债
        if (IsBondFund.BOND.getCode().equals(brProduct.getProdtClsCode())) {
            rateBondCal(obj, brProduct);
        }

        //5. 设置成已处理完成
        brProduct.setDealFlag(FileDealFlag.PROCESSED.getCode());
        dbHandler.updateProduct(brProduct);

        //6. 删除文件
        File file = new File(localFilePath);
        if(file.exists() && file.isFile()){
            file.delete();
        }
        return true;
    }

    private void failProcess(BrProduct brProduct) {
        brProduct.setDealFlag(FileDealFlag.UNPROCESSED.getCode());
        brProduct.setDealBgnTime(null);
        brProduct.setTmoutTimeNum(null);
        dbHandler.updateProduct(brProduct);
    }


    private Resource getFileResource(String filePath) {
        Resource resource = null;
        try {
            Resource[] resources = documentReader.read(filePath);
            resource = resources[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

    private boolean insertVectorStore(BrProduct brProduct, Resource resource) {
        String fundProdtFullNm = brProduct.getFundProdtFullNm();
        String fundProdtSname = brProduct.getFundProdtSname();
        String ancmTpBclsCd = brProduct.getAncmTpBclsCd();
        String fundCode = brProduct.getFundCode();

        HashMap<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("file_name_keywords", fundCode);
        conditionMap.put("file_type", ancmTpBclsCd);
        documentDeleteService.doDel(conditionMap);

        List<Document> documents = documentInsertService.processPdfResource(resource);

        List<Document> docs = documents.stream().filter(
                document -> StringUtils.isNotEmpty(document.getText()) && StringUtils.isNotEmpty(document.getText().trim())
        ).toList();
        List<Document> extendDocs = docs.stream().peek(document -> {
            Map<String, Object> metadata = document.getMetadata();
            metadata.put("file_name", fundProdtFullNm);
            metadata.put("file_type", ancmTpBclsCd);
            metadata.put("file_name_keywords", List.of(fundCode, fundProdtFullNm, fundProdtSname));
        }).toList();

        if(log.isDebugEnabled()){
            extendDocs.forEach(finalDocs -> {
                String text = finalDocs.getText();
                log.debug("内容是{}",text);
            });
        }
        return documentInsertService.insertVectorStore(extendDocs);
    }

    private Object insertPqStore(BrProduct brProduct) {
        String fundProdtFullNm = brProduct.getFundProdtFullNm();
        String fundProdtSname = brProduct.getFundProdtSname();
        String ancmTpBclsCd = brProduct.getAncmTpBclsCd();
        String fundCode = brProduct.getFundCode();
        Object obj;
        //3. 通过文件类型提取到不同的表中
        if (FundInfoType.REPORT.getCode().equals(ancmTpBclsCd)) {
            BrProductReport brProductReport = productReportQueryService.queryFundProduct(brProduct);
            brProductReport.setFundCode(fundCode);
            brProductReport.setFundFnm(fundProdtFullNm);
            brProductReport.setProdtSname(fundProdtSname);
            dbHandler.insertProductReport(brProductReport);
            obj = brProductReport;
        } else {
            BrProductSummary brProductSummary = productSummaryQueryService.queryFundProduct(brProduct);
            brProductSummary.setFundCode(fundCode);
            brProductSummary.setFundFnm(fundProdtFullNm);
            dbHandler.insertProductSummary(brProductSummary);
            obj = brProductSummary;
        }
        return obj;
    }


    private void rateBondCal(Object obj, BrProduct brProduct) {
        String fundCode = brProduct.getFundCode();

        log.info("{}开始计算利率债", fundCode);
        BrProductReport report = null;
        BrProductSummary summary = null;

        if (obj instanceof BrProductReport) {
            report = (BrProductReport) obj;
            BrProductSummary brProductSummary = new BrProductSummary();
            brProductSummary.setFundCode(fundCode);
            List<BrProductSummary> brProductSummaries = dbHandler.selectBrProductSummary(brProductSummary);
            if(CollectionUtils.isNotEmpty(brProductSummaries)){
                summary = brProductSummaries.get(0);
            }
        } else {
            summary = (BrProductSummary) obj;
            BrProductReport brProductReport = new BrProductReport();
            brProductReport.setFundCode(fundCode);
            List<BrProductReport> brProductReports = dbHandler.selectBrProductReport(brProductReport);
            if(CollectionUtils.isNotEmpty(brProductReports)){
                report = brProductReports.get(0);
            }
        }
        if (report == null || summary == null) {
            return;
        }
        Double assetNval = FundClassifierUtil.findDouble(report.getAssetNval());
        log.info("{}该基金期末资产净值是{}", fundCode,assetNval);

        if (assetNval < MIN_NVAL) {
            return;
        }
        log.info("{}开始发送模型计算利率债",fundCode);
        String[] strings = rateBondCalHandler.callModel(report, summary);
        String type = strings[0];
        BondFundType bondFundType = BondFundType.getBondFundType(type.trim());
        String code = bondFundType.getCode();
        String reason = strings[1];

        report.setClsReasonCode(code);
        report.setMainReason(reason);
        //更新季报
        dbHandler.updateProductReport(report);

        log.info("{}计算完成利率债是{},开始插入市场报告表",fundCode,code);
        //插入市场报告表
        BrMarketProductReport brMarketProductReport = new BrMarketProductReport();
        brMarketProductReport.setFundCode(fundCode);
        brMarketProductReport.setFinBondType(code);
        dbHandler.insertMarketProductReport(brMarketProductReport);
    }


    private List<List<BrProduct>> splitIntoBatches(List<BrProduct> brProducts, int bachSize) {
        List<List<BrProduct>> batches = new ArrayList<>();
        List<BrProduct> currentBatch = new ArrayList<>();
        int i = 0;
        for (BrProduct brProduct : brProducts) {
            if (i >= bachSize) {
                batches.add(currentBatch);
                currentBatch = new ArrayList<>();
                i = 0;
            }
            currentBatch.add(brProduct);
            i++;
        }
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        return batches;
    }

}

package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.rag.ProcessingMonitor;
import com.dahuaboke.mpda.bot.rag.handler.RateBondCalHandler;
import com.dahuaboke.mpda.bot.rag.service.DocumentDeleteService;
import com.dahuaboke.mpda.bot.rag.service.DocumentInsertService;
import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.rag.service.ProductSummaryQueryService;
import com.dahuaboke.mpda.bot.rag.utils.FundClassifierUtil;
import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.bot.tools.ContentManageTool;
import com.dahuaboke.mpda.bot.tools.dto.ContentManageResponse;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import com.dahuaboke.mpda.bot.tools.enums.FundInfoType;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.bot.tools.service.BrProductReportService;
import com.dahuaboke.mpda.bot.tools.service.BrProductService;
import com.dahuaboke.mpda.bot.tools.service.BrProductSummaryService;
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

    @Autowired
    private BrProductService brProductService;

    @Autowired
    private BrProductReportService brProductReportService;

    @Autowired
    BrProductSummaryService brProductSummaryService;

    private static final int SUM_TOTAL = 2500;

    private static final Long TIME_OUT = 3 * 60 * 60L;

    private static final Double MIN_NVAL = 5000000000.0;

    private static final int INSERT_BATCH = 25;

    @Scheduled(cron = "0 0 04 * * ?")
    public void ragSearchJob() {
        log.info("开始执行pdf数据处理任务.......");

        // 获取未处理文件
        List<BrProduct> brProducts = brProductService.markAndSelectUnprocessed(1500);
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
            List<List<BrProduct>> batches = FundDocUtil.splitIntoBatches(brProducts, 10);

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
                        processingMonitor.writeFailuresToFile(failures, "pdf_parse_processing");
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
        brProductService.updateProduct(brProduct);

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
        insertVectorStore(brProduct, fileResource);

        //3. 提取文件内容到对象,插入pq
        insertPqStore(brProduct);

        //4. 设置成已处理完成
        brProduct.setDealFlag(FileDealFlag.PROCESSED.getCode());
        brProductService.updateProduct(brProduct);

        //5. 删除文件
        File file = new File(localFilePath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        return true;
    }

    private void failProcess(BrProduct brProduct) {
        brProduct.setDealFlag(FileDealFlag.PROCESS_FAIL.getCode());
        brProduct.setDealBgnTime(null);
        brProduct.setTmoutTimeNum(null);
        brProductService.updateProduct(brProduct);
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

    private void insertVectorStore(BrProduct brProduct, Resource resource) {
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

        if (log.isDebugEnabled()) {
            extendDocs.forEach(finalDocs -> {
                String text = finalDocs.getText();
                log.debug("内容是{}", text);
            });
        }
        for (int i = 0; i < extendDocs.size(); i += INSERT_BATCH) {
            int min = Math.min(i + INSERT_BATCH, extendDocs.size());
            List<Document> batchDocs = extendDocs.subList(i, min);
            documentInsertService.insertVectorStore(batchDocs);
        }
    }

    private void insertPqStore(BrProduct brProduct) {
        String fundProdtFullNm = brProduct.getFundProdtFullNm();
        String fundProdtSname = brProduct.getFundProdtSname();
        String ancmTpBclsCd = brProduct.getAncmTpBclsCd();
        String fundCode = brProduct.getFundCode();
        if (FundInfoType.REPORT.getCode().equals(ancmTpBclsCd)) {
            BrProductReport brProductReport = productReportQueryService.queryFundProduct(brProduct);
            brProductReport.setFundCode(fundCode);
            brProductReport.setFundFnm(fundProdtFullNm);
            brProductReport.setProdtSname(fundProdtSname);
            brProductReportService.insertProductReport(brProductReport);
        } else {
            BrProductSummary brProductSummary = productSummaryQueryService.queryFundProduct(brProduct);
            brProductSummary.setFundCode(fundCode);
            brProductSummary.setFundFnm(fundProdtFullNm);
            brProductSummaryService.insertProductSummary(brProductSummary);
        }
    }

}

package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.rag.service.ProductReportQueryService;
import com.dahuaboke.mpda.bot.rag.service.ProductSummaryQueryService;
import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.bot.tools.entity.BrPdfParseExceptions;
import com.dahuaboke.mpda.bot.tools.entity.BrProduct;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.FileDealFlag;
import com.dahuaboke.mpda.bot.tools.enums.FundInfoType;
import com.dahuaboke.mpda.bot.tools.enums.PdfExceptionType;
import com.dahuaboke.mpda.bot.tools.service.BrPdfParseExceptionsService;
import com.dahuaboke.mpda.bot.tools.service.BrProductReportService;
import com.dahuaboke.mpda.bot.tools.service.BrProductService;
import com.dahuaboke.mpda.bot.tools.service.BrProductSummaryService;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExceptionRetryTask {

    private static final Logger log = LoggerFactory.getLogger(ExceptionRetryTask.class);

    @Autowired
    private BrPdfParseExceptionsService brPdfParseExceptionsService;

    @Autowired
    private BrProductReportService brProductReportService;

    @Autowired
    private BrProductSummaryService brProductSummaryService;

    @Autowired
    private BrProductService brProductService;

    @Autowired
    private ProductReportQueryService reportQueryService;

    @Autowired
    private ProductSummaryQueryService summaryQueryService;

    @Autowired
    private RagSearchTask ragSearchTask;

    @Scheduled(cron = "0 0 01 * * ?")
    public void exceptionRetryTask() {
        log.info("开始执行异常重试任务...........");

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                5,
                7,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5000),
                new ThreadFactoryBuilder().setNameFormat("exception-retry-%d").build()
        );

        try {
            List<BrPdfParseExceptions> fileExceptionList = brPdfParseExceptionsService.queryAllParseException();
            if (fileExceptionList.isEmpty()) {
                log.info("异常重试任务结束，无异常记录...........");
                return;
            }
            // 分批
            List<List<BrPdfParseExceptions>> batches = FundDocUtil.splitIntoBatches(fileExceptionList, 10);

            // 线程池调度
            for (List<BrPdfParseExceptions> batch : batches) {
                executorService.submit(() -> {
                    try {
                       process(batch);
                    } catch (Exception e) {
                        log.error("重试子线程{}处理批次异常", Thread.currentThread().getName(), e);
                    }
                });
            }
        } catch (Exception e) {
            throw new MpdaRuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

    private void process(List<BrPdfParseExceptions> batch){
        Map<String, List<BrPdfParseExceptions>> groupByList = batch.stream().collect(Collectors.groupingBy(BrPdfParseExceptions::getExceptionType));
        if(groupByList.containsKey(PdfExceptionType.FILE_LEVEL.getCode())){
            List<BrPdfParseExceptions> fileExceptionList = groupByList.get(PdfExceptionType.FILE_LEVEL.getCode());
            fileExceptionList.forEach(this::processFailFile);
        }
        if(groupByList.containsKey(PdfExceptionType.QUESTION_LEVEL.getCode())){
            List<BrPdfParseExceptions> questionExceptionList = groupByList.get(PdfExceptionType.QUESTION_LEVEL.getCode());
            questionExceptionList.forEach(this::processFailQuestion);
        }
    }

    private void processFailFile(BrPdfParseExceptions fileException){
        boolean successFlag = false;
        try {
            String fundCode = fileException.getFundCode();
            String ancmTpBclsCd = fileException.getAncmTpBclsCd();
            BrProduct brProduct = brProductService.selectBrProductById(fundCode, ancmTpBclsCd);
            if(StringUtils.isNotEmpty(brProduct.getFundCode())){
                successFlag = ragSearchTask.process(brProduct);
            }
        } catch (Exception e) {
            log.error("{}:{} 文件补偿处理失败", fileException.getFundCode(),fileException.getAncmTpBclsCd(),e);
        } finally {
            updateExceptionTable(fileException,successFlag);
        }
    }

    private void processFailQuestion(BrPdfParseExceptions questionException){
        boolean successFlag = false;
        try {
            String fundCode = questionException.getFundCode();
            String ancmTpBclsCd = questionException.getAncmTpBclsCd();
            String question = questionException.getQuestion();
            //先查询出来该数据，再更新
            if(FundInfoType.REPORT.getCode().equals(ancmTpBclsCd)){
                List<BrProductReport> brProductReports = brProductReportService.selectBrProductReportByCodes(List.of(fundCode));
                BrProductReport report = brProductReports.get(0);
                report = reportQueryService.querySingleQuestion(report, ancmTpBclsCd, question);
                brProductReportService.updateProductReport(report);
            }else {
                List<BrProductSummary> brProductSummaries = brProductSummaryService.selectBrProductSummaryByCodes(List.of(fundCode));
                BrProductSummary summary = brProductSummaries.get(0);
                summary = summaryQueryService.querySingleQuestion(summary, ancmTpBclsCd, question);
                brProductSummaryService.updateProductSummary(summary);
            }
            successFlag = true;
        } catch (Exception e) {
            log.error("{}:{} 问题{} 补偿处理失败", questionException.getFundCode(),questionException.getAncmTpBclsCd(),questionException.getQuestion(),e);
        }finally {
            updateExceptionTable(questionException,successFlag);
        }
    }

    private void updateExceptionTable(BrPdfParseExceptions brPdfParseExceptions,boolean successFlag){
        if(successFlag){
            brPdfParseExceptionsService.delParseException(brPdfParseExceptions);
            return;
        }
        int count = brPdfParseExceptions.getCount();
        brPdfParseExceptions.setCount(++count);
        brPdfParseExceptionsService.updateParseException(brPdfParseExceptions);
    }

}

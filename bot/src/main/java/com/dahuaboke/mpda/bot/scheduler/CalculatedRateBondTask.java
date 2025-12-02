package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.rag.handler.RateBondCalHandler;
import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.bot.tools.service.BrMarketProductReportService;
import com.dahuaboke.mpda.bot.tools.service.BrProductReportService;
import com.dahuaboke.mpda.bot.tools.service.BrProductService;
import com.dahuaboke.mpda.bot.tools.service.BrProductSummaryService;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CalculatedRateBondTask {

    private static final Logger log = LoggerFactory.getLogger(CalculatedRateBondTask.class);

    @Autowired
    private BrMarketProductReportService brMarketProductReportService;

    @Autowired
    private BrProductService brProductService;

    @Autowired
    private BrProductReportService brProductReportService;

    @Autowired
    BrProductSummaryService brProductSummaryService;

    @Autowired
    private RateBondCalHandler rateBondCalHandler;

    private static final Double MIN_NVAL = 5000000000.0;

    private  Set<String> marketProductCode;


    @Scheduled(cron = "0 0 21 * * ?")
    public void calculatedRateBondJob() {
        log.info("开始计算利率债任务...........");
        init();
        List<String> rateFundCodes = brProductService.getRateFundCode(FundType.BOND_FUND.getCode());
        if(CollectionUtils.isEmpty(rateFundCodes)){
            return;
        }
        processDataList(rateFundCodes);
    }

    public void calculatedRateBondJob(List<String> rateFundCodes) {
        log.info("开始计算利率债任务...........");
        init();
        rateBondCal(rateFundCodes);
    }

    private void init(){
        marketProductCode = brMarketProductReportService.getMarketProductCode();
    }

    public void processDataList(List<String> fundCodes) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                10,
                15,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5000),
                new ThreadFactoryBuilder().setNameFormat("calculate-rate-%d").build()
        );

        try {
            // 分批
            List<List<String>> batches = FundDocUtil.splitIntoBatches(fundCodes, 10);

            // 线程池调度
            for (List<String> batch : batches) {
                executorService.submit(() -> {
                    try {
                        rateBondCal(batch);
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

    private void rateBondCal(List<String> rateFundCodes){
        if(CollectionUtils.isEmpty(rateFundCodes)){
            return;
        }
        //只查询一次
        List<BrProductReport> brProductReports = brProductReportService.selectBrProductReportByCodes(rateFundCodes);
        Map<String, BrProductReport> brProductMap = brProductReports.stream()
                .collect(Collectors.toMap(BrProductReport::getFundCode, Function.identity()));
        List<BrProductSummary> brProductSummaries = brProductSummaryService.selectBrProductSummaryByCodes(rateFundCodes);
        Map<String, BrProductSummary> brSummaryMap = brProductSummaries.stream()
                .collect(Collectors.toMap(BrProductSummary::getFundCode, Function.identity()));

        for (String fundCode: rateFundCodes){
            try {
                BrProductReport report = brProductMap.get(fundCode);
                BrProductSummary summary = brSummaryMap.get(fundCode);
                if (report != null && summary != null) {
                    boolean isRateBond = rateBondCalHandler.calculateRateBond(fundCode, report, summary);
                    if(!isRateBond &&  CollectionUtils.isNotEmpty(marketProductCode) && marketProductCode.contains(fundCode) ){
                        //如果是非利率债,需要查看市场报告表中是否存在数据，如果存在需要删除。
                        brMarketProductReportService.delMarketProductByCode(fundCode);
                    }
                }
            } catch (Exception e) {
                log.error("子线程批次{}中的基金{}处理异常", Thread.currentThread().getName(), fundCode,e);
            }
        }
    }



}

package com.dahuaboke.mpda.bot.scheduler;

import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.service.BrMarketProductReportService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketRankTask {

    private static final Logger log = LoggerFactory.getLogger(MarketRankTask.class);

    @Autowired
    BrMarketProductReportService brMarketProductReportService;

    @Autowired
    ProductToolHandler productToolHandler;

    @Scheduled(cron = "0 0 07 * * ?")
    public void marketRankJob() {
        log.info("开始执行市场产品报告任务...........");

        //1. 扫描市场报告表
        List<BrMarketProductReport> brMarketProductReports = brMarketProductReportService.selectAllMarketProductReport();
        if (brMarketProductReports.isEmpty()) {
            log.info("市场产品报告表不存在数据,执行结束...........");
            return;
        }

        //2. 通过基金代码查询净值表，计算利率债和最大回撤
        rateAndDrawal(brMarketProductReports);

        //3. 计算排名
        yesterdayRank(brMarketProductReports);
        todayRank(brMarketProductReports);
        rankChange(brMarketProductReports);

        //4. 更新排名
        brMarketProductReports.forEach(report -> brMarketProductReportService.insertMarketProductReport(report));

        log.info("执行市场产品报告任务结束...........");
    }


    private void rateAndDrawal(List<BrMarketProductReport> brMarketProductReports) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weak = now.plusDays(-7);
        LocalDateTime mouth1 = now.plusMonths(-1);
        LocalDateTime mouth3 = now.plusMonths(-3);
        LocalDateTime year = now.plusYears(-1);
        LocalDateTime yearStart = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);

        List<LocalDateTime> quarterStart = getQuarterStart();
        LocalDateTime quarter1Start = quarterStart.get(0);
        LocalDateTime quarter1End = quarter1Start.plusMonths(3).minusSeconds(1);
        LocalDateTime quarter2Start = quarterStart.get(1);
        LocalDateTime quarter2End = quarter2Start.plusMonths(3).minusSeconds(1);
        LocalDateTime quarter3Start = quarterStart.get(2);
        LocalDateTime quarter3End =  quarter3Start.plusMonths(3).minusSeconds(1);
        LocalDateTime quarter4Start = quarterStart.get(3);
        LocalDateTime quarter4End = quarter4Start.plusMonths(3).minusSeconds(1);


        brMarketProductReports.forEach(brMarketProductReport -> {
            String fundCode = brMarketProductReport.getFundCode();
            try {
                //周,月,三月,年,今年以来 收益率
                String weakRate = productToolHandler.yearRita(new NetValReq(fundCode, weak.format(yyyyMMdd), now.format(yyyyMMdd)));
                String mouth1Rate = productToolHandler.yearRita(new NetValReq(fundCode, mouth1.format(yyyyMMdd), now.format(yyyyMMdd)));
                String mouth3Rate = productToolHandler.yearRita(new NetValReq(fundCode, mouth3.format(yyyyMMdd), now.format(yyyyMMdd)));
                String yearRate = productToolHandler.yearRita(new NetValReq(fundCode, year.format(yyyyMMdd), now.format(yyyyMMdd)));
                String yearToDateRate = productToolHandler.yearRita(new NetValReq(fundCode, yearStart.format(yyyyMMdd), now.format(yyyyMMdd)));
                //今年以来(年化)
                double rate = parseRate(yearToDateRate);
                long days = ChronoUnit.DAYS.between(yearStart, now);
                double annualizedRate = Math.pow(1 + rate /100 ,365.0 /days) -1;
                String annualizedRateStr = String.format("%.2f%%",annualizedRate * 100);

                //四季度 收益率
                String quarter1Rate = productToolHandler.yearRita(new NetValReq(fundCode, quarter1Start.format(yyyyMMdd), quarter1End.format(yyyyMMdd)));
                String quarter2Rate = productToolHandler.yearRita(new NetValReq(fundCode, quarter2Start.format(yyyyMMdd), quarter2End.format(yyyyMMdd)));
                String quarter3Rate = productToolHandler.yearRita(new NetValReq(fundCode, quarter3Start.format(yyyyMMdd), quarter3End.format(yyyyMMdd)));
                String quarter4Rate = productToolHandler.yearRita(new NetValReq(fundCode, quarter4Start.format(yyyyMMdd), quarter4End.format(yyyyMMdd)));

                //周,月,三月,年 回撤
                String weakWithDrawal = productToolHandler.maxWithDrawal(new NetValReq(fundCode, weak.format(yyyyMMdd), now.format(yyyyMMdd)));
                String mouth1WithDrawal = productToolHandler.maxWithDrawal(new NetValReq(fundCode, mouth1.format(yyyyMMdd), now.format(yyyyMMdd)));
                String mouth3WithDrawal = productToolHandler.maxWithDrawal(new NetValReq(fundCode, mouth3.format(yyyyMMdd), now.format(yyyyMMdd)));
                String yearWithDrawal = productToolHandler.maxWithDrawal(new NetValReq(fundCode, year.format(yyyyMMdd), now.format(yyyyMMdd)));

                //赋值更新
                brMarketProductReport.setNwk1CombProfrat(weakRate);
                brMarketProductReport.setNmm1CombProfrat(mouth1Rate);
                brMarketProductReport.setNmm3CombProfrat(mouth3Rate);
                brMarketProductReport.setDrtPftrtTval(yearToDateRate);
                brMarketProductReport.setPftrtName(annualizedRateStr);
                brMarketProductReport.setNyy1Profrat(yearRate);
                brMarketProductReport.setLastYrlyPftrt(quarter1Rate);
                brMarketProductReport.setNmm6CombProfrat(quarter2Rate);
                brMarketProductReport.setNmm3YrlyPftrt(quarter3Rate);
                brMarketProductReport.setNmm1YearlyProfrat(quarter4Rate);
                brMarketProductReport.setStyoMaxWdwDesc(weakWithDrawal);
                brMarketProductReport.setMaxWdwrt(mouth1WithDrawal);
                brMarketProductReport.setFundstgMaxWdwrt(mouth3WithDrawal);
                brMarketProductReport.setNyy1Wdwrt(yearWithDrawal);
                brMarketProductReportService.insertMarketProductReport(brMarketProductReport);
            } catch (Exception e) {
                log.error("{}计算利率债和最大回撤失败",fundCode, e);
            }
        });
    }

    private List<LocalDateTime> getQuarterStart(){
        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        //当前年已完整结束的季度数
        int finishedQuarters;
        if(currentMonth <= 3){
            finishedQuarters = 0;
        }else if (currentMonth <= 6){
            finishedQuarters = 1;
        }else if (currentMonth <= 9){
            finishedQuarters = 2;
        }else {
            finishedQuarters = 3;
        }
        int[][] quarterMonthRanges = {{1,3},{4,6},{7,9},{10,12}};
        for (int quarterIdx = 0; quarterIdx < 4; quarterIdx++) {
            int[] monthRange = quarterMonthRanges[quarterIdx];
            int targetYear;
            if(quarterIdx < finishedQuarters){
                targetYear = currentYear;
            }else {
                targetYear = currentYear - 1;
            }
            LocalDateTime quarterStart = LocalDateTime.of(targetYear, monthRange[0], 1, 0, 0, 0);
            localDateTimeList.add(quarterStart);
        }
        return localDateTimeList;
    }

    private void yesterdayRank(List<BrMarketProductReport> brMarketProductReports) {
        brMarketProductReports.forEach(report -> {
            int indsRankSeqNo = report.getIndsRankSeqNo();
            report.setTxamtRankNo(indsRankSeqNo);

            int lblmRank = report.getLblmRank();
            report.setBusicmLmRank(lblmRank);

            int rankScopeLowLmtVal = report.getRankScopeLowLmtVal();
            report.setIntglRankSeqNo(rankScopeLowLmtVal);

            int reachStRankSeqNo = report.getReachStRankSeqNo();
            report.setChremMgrIntglRankSeqNo(reachStRankSeqNo);
        });
    }

    private void todayRank(List<BrMarketProductReport> brMarketProductReports) {
        var groupByFindBondType = brMarketProductReports.stream().collect(Collectors.groupingBy(BrMarketProductReport::getFinBondType));
        groupByFindBondType.forEach((type, funds) -> {
            sortAndRank(funds, BrMarketProductReport::getNwk1CombProfrat, BrMarketProductReport::setIndsRankSeqNo);
            sortAndRank(funds, BrMarketProductReport::getNmm1CombProfrat, BrMarketProductReport::setLblmRank);
            sortAndRank(funds, BrMarketProductReport::getNmm3CombProfrat, BrMarketProductReport::setRankScopeLowLmtVal);
            sortAndRank(funds, BrMarketProductReport::getDrtPftrtTval, BrMarketProductReport::setRtnRtRank);
            sortAndRank(funds, BrMarketProductReport::getPftrtName, BrMarketProductReport::setBusicmOybinpRank);
            sortAndRank(funds, BrMarketProductReport::getNyy1Profrat, BrMarketProductReport::setReachStRankSeqNo);

            sortAndRank(funds, BrMarketProductReport::getLastYrlyPftrt, BrMarketProductReport::setCustRaiseRateRankNo);
            sortAndRank(funds, BrMarketProductReport::getNmm6CombProfrat, BrMarketProductReport::setDetainRateRankNo);
            sortAndRank(funds, BrMarketProductReport::getNmm3YrlyPftrt, BrMarketProductReport::setTmPontAsetRaiseTotRanknum);
            sortAndRank(funds, BrMarketProductReport::getNmm1YearlyProfrat, BrMarketProductReport::setAddRepPurcProTotnumRankno);
        });
    }

    /**
     * 昨日排名为0,代表没有值,今日排名是首次
     */
    private void rankChange(List<BrMarketProductReport> brMarketProductReports) {
        brMarketProductReports.forEach(report -> {
            int indsRankSeqNo = report.getIndsRankSeqNo();
            int txamtRankNo = report.getTxamtRankNo();
            if (txamtRankNo == 0) {
                report.setCurdayBalChgTotalAccnum(0);
            } else {
                report.setCurdayBalChgTotalAccnum(txamtRankNo - indsRankSeqNo);
            }

            int lblmRank = report.getLblmRank();
            int busicmLmRank = report.getBusicmLmRank();
            if (busicmLmRank == 0) {
                report.setCurdayBalChgAccnum(0);
            } else {
                report.setCurdayBalChgAccnum(busicmLmRank - lblmRank);
            }

            int rankScopeLowLmtVal = report.getRankScopeLowLmtVal();
            int intglRankSeqNo = report.getIntglRankSeqNo();
            if (intglRankSeqNo == 0) {
                report.setSupptranBalChgTotalAccnum(0);
            } else {
                report.setSupptranBalChgTotalAccnum(intglRankSeqNo - rankScopeLowLmtVal);
            }

            int reachStRankSeqNo = report.getReachStRankSeqNo();
            int chremMgrIntglRankSeqNo = report.getChremMgrIntglRankSeqNo();
            if (chremMgrIntglRankSeqNo == 0) {
                report.setCenterCfmCurdayChgTnum(0);
            } else {
                report.setCenterCfmCurdayChgTnum(chremMgrIntglRankSeqNo - reachStRankSeqNo);
            }
        });
    }


    private void sortAndRank(List<BrMarketProductReport> list,
                             Function<BrMarketProductReport, String> rateGetter,
                             BiConsumer<BrMarketProductReport, Integer> rankSetter) {
        list.sort((f1, f2) -> {
            double r1 = parseRate(rateGetter.apply(f1));
            double r2 = parseRate(rateGetter.apply(f2));
            return Double.compare(r2, r1);
        });

        for (int i = 0; i < list.size(); i++) {
            rankSetter.accept(list.get(i), i + 1);
        }
    }

    private double parseRate(String rateStr) {
        if (rateStr == null || rateStr.trim().isEmpty()) {
            return Double.NEGATIVE_INFINITY;
        }
        try {
            return Double.parseDouble(rateStr.replaceAll("%$", ""));
        } catch (NumberFormatException e) {
            return Double.NEGATIVE_INFINITY;
        }
    }

}

package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.tools.dao.*;
import com.dahuaboke.mpda.bot.tools.dto.*;
import com.dahuaboke.mpda.bot.tools.entity.*;
import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.bot.tools.enums.ShelfStatus;
import com.dahuaboke.mpda.bot.tools.enums.TimeType;
import com.dahuaboke.mpda.bot.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.IsoFields.QUARTER_OF_YEAR;

public class RobotService {

    private static final Logger log = LoggerFactory.getLogger(RobotService.class);

    @Autowired
    private BrProductReportMapper brProductReportMapper;

    @Autowired
    private BrProductSummaryMapper brProductSummaryMapper;

    @Autowired
    private BrNetvalueMapper brNetvalueMapper;

    @Autowired
    private BrMarketProductReportMapper brMarketProductReportMapper;

    @Autowired
    private FdHolidayMapper fdHolidayMapper;

    @Autowired
    private BrProductMapper brProductMapper;

    private static final Pattern pattern = Pattern.compile(
            "(\\d{4})(年|[-/])" +
                    "(\\d{1,2})(月|[-/])" +
                    "(\\d{1,2})(日|[-/]?)", Pattern.DOTALL);

    private static final int CRITICAL_WORKDAY_NUM = 15; // 季度初第15个工作日

    public ProdInfoDto getInfo(String code) {
        ProdInfoDto prodInfoDto = new ProdInfoDto();
        LambdaQueryWrapper<BrProductReport> queryWrapper = new LambdaQueryWrapper<BrProductReport>()
                .eq(BrProductReport::getFundCode, code);
        List<BrProductReport> brProductReports = brProductReportMapper.selectList(queryWrapper);

        LambdaQueryWrapper<BrProductSummary> queryWrapper1 = new LambdaQueryWrapper<BrProductSummary>()
                .eq(BrProductSummary::getFundCode, code);
        List<BrProductSummary> brProductSummaries = brProductSummaryMapper.selectList(queryWrapper1);

        BrProductReport report = new BrProductReport();
        BrProductSummary summary = new BrProductSummary();
        if (!brProductReports.isEmpty()) {
            report = brProductReports.get(0);
        }
        if (!brProductSummaries.isEmpty()) {
            summary = brProductSummaries.get(0);
        }

        BeanUtils.copyProperties(report, prodInfoDto);
        BeanUtils.copyProperties(summary, prodInfoDto);
        return prodInfoDto;
    }


    public String getWithDrawal(NetValReq netValReq) {
        List<BrNetvalue> ywjqrNetvalues = brNetvalueMapper.selectList(new LambdaQueryWrapper<BrNetvalue>()
                .eq(BrNetvalue::getFundCode, netValReq.getProdtCode())
                .between(BrNetvalue::getNetValDate, netValReq.getBegDate(), netValReq.getEndDate())
                .orderByAsc(BrNetvalue::getNetValDate));
        if (ywjqrNetvalues.size() == 0) {
            return "" + 0;
        }
        String maxNetVal = ywjqrNetvalues.get(0).getUnitNetVal();
        String ans = "0";
        for (BrNetvalue fund : ywjqrNetvalues) {
            if (fund.getUnitNetVal().compareTo(maxNetVal) > 0) {
                maxNetVal = fund.getUnitNetVal();
            } else {
                BigDecimal bigDecimal1 = new BigDecimal(maxNetVal);
                BigDecimal bigDecimal2 = new BigDecimal(fund.getUnitNetVal());
                BigDecimal divide = bigDecimal1.subtract(bigDecimal2).divide(bigDecimal1, new MathContext(7, RoundingMode.HALF_UP));
                ans = ans.compareTo(String.valueOf(divide)) > 0 ? ans : String.valueOf(divide);
            }
        }
        return ans;
    }


    public String yearRita(NetValReq netValReq) {
        if (StringUtils.isEmpty(netValReq.getEndDate())) {
            netValReq.setEndDate(DateUtil.getBusinessToday());
        }
        if (StringUtils.isEmpty(netValReq.getBegDate())) {
            String txDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30);
            netValReq.setBegDate(txDate);
        }
        List<BrNetvalue> ywjqrNetvalues = brNetvalueMapper.selectList(new LambdaQueryWrapper<BrNetvalue>()
                .eq(BrNetvalue::getFundCode, netValReq.getProdtCode())
                .between(BrNetvalue::getNetValDate, netValReq.getBegDate(), netValReq.getEndDate())
                .orderByAsc(BrNetvalue::getNetValDate));
        if (ywjqrNetvalues.size() == 0) {
            return "" + 0;
        }
        String one = ywjqrNetvalues.get(0).getUnitNetVal();
        String last = ywjqrNetvalues.get(ywjqrNetvalues.size() - 1).getUnitNetVal();
        BigDecimal bigDecimal1 = new BigDecimal(one);
        BigDecimal bigDecimal2 = new BigDecimal(last);
        BigDecimal divide = bigDecimal2.subtract(bigDecimal1).divide(bigDecimal1, new MathContext(7, RoundingMode.HALF_UP));
        return String.valueOf(divide);
    }

    public Map<String, List<String>> getMap() {
        Map<String, List<String>> map = new HashMap<>();
        List<BrProductReport> brProductReports = brProductReportMapper.selectList(new LambdaQueryWrapper<BrProductReport>());
        for (BrProductReport report : brProductReports) {
            map.put(report.getFundCode(), List.of(report.getFundFnm(), report.getProdtSname()));
        }
        return map;
    }

    public List<MarketRankDto> getMarketRank(String finBondType, String period) {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>();
        queryWrapper.last("LIMIT " + 10);
        List<MarketRankDto> rankDtos = selectMarketReportByTimeAndFundType(finBondType, period, queryWrapper);
        return rankDtos;
    }

    /**
     * 通过债基类型和时间查询市场报告
     * ESBxi下载入口
     *
     * @param finBondType
     * @param period
     * @return
     */
    public List<MarketRankDto> selectMarketReportByTimeAndFundType(String finBondType, String period) {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>();
        return selectMarketReportByTimeAndFundType(finBondType, period, queryWrapper);
    }

    /**
     * 通过债基类型和时间查询市场报告
     *
     * @param finBondType
     * @param period
     * @param queryWrapper
     * @return
     */
    public List<MarketRankDto> selectMarketReportByTimeAndFundType(String finBondType, String period, LambdaQueryWrapper<BrMarketProductReport> queryWrapper) {
        queryWrapper.eq(BrMarketProductReport::getFinBondType, finBondType);
        if (TimeType.LAST_WEEK.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getIndsRankSeqNo);
        } else if (TimeType.LAST_MONTH.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getLblmRank);
        } else if (TimeType.LAST_3_MONTH.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getRankScopeLowLmtVal);
        } else if (TimeType.LAST_YEAR.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getReachStRankSeqNo);
        } else if (TimeType.CURRENT_YEAR_Q1.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getCustRaiseRateRankNo);
        } else if (TimeType.CURRENT_YEAR_Q2.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getDetainRateRankNo);
        } else if (TimeType.CURRENT_YEAR_Q3.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getTmPontAsetRaiseTotRanknum);
        } else if (TimeType.CURRENT_YEAR_Q4.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getAddRepPurcProTotnumRankno);
        } else if (TimeType.CURRENT_YEAR.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getRtnRtRank);
        } else if (TimeType.NONE.getCode().equals(period) || "".equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getRtnRtRank);
        }
        List<BrMarketProductReport> brMarketProductReports = brMarketProductReportMapper.selectList(queryWrapper);
        ArrayList<MarketRankDto> rankDtos = new ArrayList<>();
        if (brMarketProductReports.size() == 0) {
            return rankDtos;
        }
        brMarketProductReports.forEach(report -> {
            MarketRankDto marketRankDto = new MarketRankDto();
            String fundCode = report.getFundCode();
            ProdInfoDto info = getInfo(fundCode);
            BeanUtils.copyProperties(info, marketRankDto);
            BeanUtils.copyProperties(report, marketRankDto);
            rankDtos.add(marketRankDto);
        });

        //提取基金代码列表
        List<String> fundCodes = rankDtos.stream()
                .map(MarketRankDto::getFundCode)
                .collect(Collectors.toList());
        //单位净值
        List<BrNetvalueDto> brNetvalues = brNetvalueMapper.selectBrNetvalueLatest(fundCodes);
        Map<String, String> brNetvaluesMap = brNetvalues.stream().collect(Collectors.toMap(BrNetvalueDto::getFundCode, BrNetvalueDto::getUnitNetVal));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        rankDtos.stream().forEach(rankDto -> {
            //当前日期
            rankDto.setCurDate(now.format(dateTimeFormatter));
            //TODO下载文件名
            rankDto.setDldFlnm("全市场" + BondFundType.getBondFundTypeDesc(finBondType).getDesc() + "排名" + dealDldFlnm(LocalDate.now()));
            //基金成立日
            rankDto.setContractEffDate(dealContractEffDate(rankDto.getContractEffDate()));
            //存续天数
            rankDto.setCurRenewDt(dealcurRenewDt(rankDto.getContractEffDate()));
            //债券基金类型
            rankDto.setFinBondType(BondFundType.getBondFundTypeDesc(finBondType).getDesc());
            rankDto.setPeriod(period);
            //单位净值
            rankDto.setUnitNetVal(brNetvaluesMap.get(rankDto.getFundCode()));
            //基金规模
            rankDto.setAssetNval(dealAssetNval(rankDto.getAssetNval()));
        });
        return rankDtos;
    }

    public String dealAssetNval(String assetNval) {
        double number = Double.parseDouble(assetNval);
        if (number == 0) {
            return "0";
        } else {
            double inBillions = number / 100_000_000;
            return String.format("%.2f", inBillions) + "亿";
        }
    }

    /**
     * 处理基金成立日
     *
     * @param contractEffDate
     * @return
     */
    public String dealContractEffDate(String contractEffDate) {
        String contractEffDateFinal = "";
        try {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(contractEffDate)) {
                contractEffDate = contractEffDate.replaceAll("\\s+", "");
                Matcher matcher = pattern.matcher(contractEffDate);
                if (matcher.find()) {
                    contractEffDateFinal = matcher.group(1)
                            + String.format("%02d", Integer.parseInt(matcher.group(3)))
                            + String.format("%02d", Integer.parseInt(matcher.group(5)));
                }
            }
        } catch (Exception e) {
            log.error("处理基金成立日失败，contractEffDate为{}", contractEffDate, e);
            return contractEffDateFinal;
        }
        return contractEffDateFinal;
    }

    /**
     * 续存天数
     *
     * @param contractEffDate
     * @return
     */
    public int dealcurRenewDt(String contractEffDate) {
        int between = 0;
        try {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(contractEffDate)) {
                LocalDate target = LocalDate.parse(contractEffDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                LocalDate today = LocalDate.now();
                between = (int) ChronoUnit.DAYS.between(target, today);
            }
        } catch (Exception e) {
            log.error("续存天数计算失败，contractEffDate为{}", contractEffDate, e);
            return between;
        }
        return between;
    }

    /**
     * 处理下载文件名
     * 在季度初15个工作日内命名的季度为上上季度，15个工作日后命名为上季度。比如，10.1-10.28（15个工作日）报告名称为《全市场利率债主动-开放式排名2025Q2》，10.29之后，命名为《全市场利率债主动-开放式排名2025Q3》
     * 工作日的计算，可以通过读取理财系统的节假日表，读取业务类型=基金的，基金是证券间日历
     *
     * @return
     */
    @DS("db1")
    public String dealDldFlnm(LocalDate localDate) {
        LambdaQueryWrapper<FdHoliday> queryWrapper = new LambdaQueryWrapper<FdHoliday>();
        String year = Year.now().getValue() + "";
        queryWrapper.like(FdHoliday::getThedate, year);
        queryWrapper.eq(FdHoliday::getFundworkday, "1");
        List<FdHoliday> fdHolidays = fdHolidayMapper.selectList(queryWrapper);
        String DldFlnm = generateReportName(fdHolidays, localDate);
        return DldFlnm;
    }


    /**
     * 计算从起始日期开始的第N个工作日
     *
     * @param startDate 起始日期
     * @param n         第N个工作日（正整数）
     * @return 目标工作日
     */
    public LocalDate getNthWorkday(List<FdHoliday> fdHolidays, LocalDate startDate, int n) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = startDate;
        //String today = startDate.format(formatter);
        Set<LocalDate> fdHolidaysSet = fdHolidays.stream()
                .map(FdHoliday::getThedate)
                .filter(thedate -> thedate != null && !thedate.isEmpty())
                .map(thedate -> LocalDate.parse(thedate, formatter))
                .collect(Collectors.toSet());
        if (n <= 0) {
            throw new IllegalArgumentException("工作日数必须为正整数");
        }

        int workdayCount = 0;
        while (true) {
            // 判断是否为工作日：非周末且不在节假日列表
            if (!fdHolidaysSet.contains(currentDate)) {
                workdayCount++;
                if (workdayCount == n) {
                    return currentDate;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    /**
     * 生成指定日期的报告名称
     *
     * @param fdHolidays 节假日
     * @param today      指定日期
     * @return 报告名称（格式：《年份Q季度》）
     */
    public String generateReportName(List<FdHoliday> fdHolidays, LocalDate today) {
        // 1. 获取当前日期所在季度的季度初日期
        LocalDate quarterStart = getQuarterStartDate(today);
        // 2. 计算季度初第15个工作日（临界点）
        LocalDate criticalDate = getNthWorkday(fdHolidays, quarterStart, CRITICAL_WORKDAY_NUM);
        // 3. 确定季度标识
        String quarterCode = getTargetQuarterCode(today, criticalDate, quarterStart);
        return quarterCode;
    }

    /**
     * 获取指定日期所在季度的季度初日期
     *
     * @param date 指定日期
     * @return 季度初日期（如Q1为1月1日，Q2为4月1日等）
     */
    private LocalDate getQuarterStartDate(LocalDate date) {
        int year = date.getYear();
        int quarter = date.get(QUARTER_OF_YEAR);
        int startMonth = (quarter - 1) * 3 + 1; // Q1=1, Q2=4, Q3=7, Q4=10
        return LocalDate.of(year, startMonth, 1);
    }

    /**
     * 确定报告名称的季度标识
     * 逻辑：
     * - 15个工作日内：使用「当前季度的上上个季度」（如2025Q1 → 2024Q3）
     * - 15个工作日后：使用「当前季度的上一个季度」（如2025Q1 → 2024Q4）
     */
    private String getTargetQuarterCode(LocalDate today, LocalDate criticalDate, LocalDate quarterStart) {
        int currentYear = quarterStart.getYear();
        int currentQuarter = quarterStart.get(QUARTER_OF_YEAR);

        if (today.isBefore(criticalDate) || today.isEqual(criticalDate)) {
            // 15个工作日内：当前季度的上上个季度
            int targetQuarter = currentQuarter - 2;
            int targetYear = currentYear;
            if (targetQuarter <= 0) {
                // 跨年处理（如Q1-2=-1 → 上一年Q3；Q2-2=0 → 上一年Q4）
                targetYear = currentYear - 1;
                targetQuarter += 4;
            }
            return String.format("%dQ%d", targetYear, targetQuarter);
        } else {
            // 15个工作日后：当前季度的上一个季度
            if (currentQuarter == 1) {
                return String.format("%dQ%d", currentYear - 1, 4);
            } else {
                return String.format("%dQ%d", currentYear, currentQuarter - 1);
            }
        }
    }

    public List<RecommendProductDto> getFundInfoByType(String fundType) {
        LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                .select(BrProduct::getFundCode)
                .eq(BrProduct::getProdtClsCode, fundType)
                .eq(BrProduct::getValidFlag, ShelfStatus.ON_SHELF.getCode());
        List<String> fundCodes = brProductMapper.selectObjs(queryWrapper).stream().map(Object::toString).toList();
        if (CollectionUtils.isEmpty(fundCodes)) {
            return List.of();
        }
        List<RecommendProductDto> recommendProductDtos = brProductMapper.selectFundDetail(fundCodes);
        if (CollectionUtils.isEmpty(recommendProductDtos)) {
            return List.of();
        }
        return recommendProductDtos.stream().peek(recommendProductDto -> recommendProductDto.setProdtClsCode(FundType.getFundTypeDesc(fundType).getDesc())).toList();
    }

    public String sevenDayYearlyProfrat(NetValReq netValReq) {
        LambdaQueryWrapper<BrNetvalue> queryWrapper = new LambdaQueryWrapper<BrNetvalue>()
                .eq(BrNetvalue::getFundCode, netValReq.getProdtCode())
                .orderByDesc(BrNetvalue::getNetValDate)
                .last("LIMIT 1");

        List<BrNetvalue> netValues = brNetvalueMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(netValues)) {
            return "0";
        }
        return netValues.get(0).getStgyD7YearlyProfrat();
    }


}

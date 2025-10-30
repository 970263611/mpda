package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.tools.dao.*;
import com.dahuaboke.mpda.bot.tools.dto.*;
import com.dahuaboke.mpda.bot.tools.entity.*;
import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.bot.tools.enums.TimeType;
import com.dahuaboke.mpda.core.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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


    public List<ProdInfoDto> filterProdInfo(FilterProdInfoReq filterProdInfoReq) {
        List<ProdInfoDto> list = new ArrayList<>();

        LambdaQueryWrapper<BrProductReport> queryWrapper = new LambdaQueryWrapper<BrProductReport>()
                .eq(BrProductReport::getClsReasonCode, filterProdInfoReq.getFundClassificationCode());
        List<BrProductReport> brProductReports = brProductReportMapper.selectList(queryWrapper);


        for (BrProductReport brProductReport : brProductReports) {
            String fundCode = brProductReport.getFundCode();

            if (StringUtils.isEmpty(filterProdInfoReq.getWithDrawal()) && StringUtils.isEmpty(filterProdInfoReq.getYearRita())) {
                ProdInfoDto info = getInfo(fundCode);
                list.add(info);
                continue;
            }

            NetValReq netValReq = new NetValReq();
            netValReq.setBegDate(DateUtil.getBusinessToday());
            netValReq.setEndDate(DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30));
            netValReq.setProdtCode(fundCode);

            String withDrawal = getWithDrawal(netValReq);
            String yearRita = yearRita(netValReq);

            if (!StringUtils.isEmpty(filterProdInfoReq.getWithDrawal()) && !StringUtils.isEmpty(filterProdInfoReq.getYearRita())) {
                if (filterProdInfoReq.getWithDrawal().contains("%"))
                    filterProdInfoReq.setWithDrawal(filterProdInfoReq.getWithDrawal().replace("%", ""));
                if (filterProdInfoReq.getYearRita().contains("%"))
                    filterProdInfoReq.setYearRita(filterProdInfoReq.getYearRita().replace("%", ""));
                BigDecimal bigDecimal1 = new BigDecimal(filterProdInfoReq.getWithDrawal()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal2 = new BigDecimal(filterProdInfoReq.getYearRita()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal3 = new BigDecimal(withDrawal);
                BigDecimal bigDecimal4 = new BigDecimal(yearRita);
                //1<3返回负数
                if (bigDecimal1.compareTo(bigDecimal3) > 0 && bigDecimal2.compareTo(bigDecimal4) < 0) {
                    ProdInfoDto info = getInfo(fundCode);
                    list.add(info);
                }
                continue;
            }
            if (!StringUtils.isEmpty(filterProdInfoReq.getWithDrawal())) {
                if (filterProdInfoReq.getWithDrawal().contains("%"))
                    filterProdInfoReq.setWithDrawal(filterProdInfoReq.getWithDrawal().replace("%", ""));
                BigDecimal bigDecimal1 = new BigDecimal(filterProdInfoReq.getWithDrawal()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal3 = new BigDecimal(withDrawal);
                if (bigDecimal1.compareTo(bigDecimal3) > 0) {
                    ProdInfoDto info = getInfo(fundCode);
                    list.add(info);

                }
                continue;
            }
            if (!StringUtils.isEmpty(filterProdInfoReq.getYearRita())) {
                if (filterProdInfoReq.getYearRita().contains("%"))
                    filterProdInfoReq.setYearRita(filterProdInfoReq.getYearRita().replace("%", ""));
                BigDecimal bigDecimal2 = new BigDecimal(filterProdInfoReq.getYearRita()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal4 = new BigDecimal(yearRita);
                if (bigDecimal2.compareTo(bigDecimal4) < 0) {
                    ProdInfoDto info = getInfo(fundCode);
                    list.add(info);
                }
            }
        }
        return list;
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
//        List<BrMarketProductReport> brMarketProductReports = brMarketProductReportMapper.selectList(queryWrapper);
//        ArrayList<MarketRankDto> rankDtos = new ArrayList<>();
//        brMarketProductReports.forEach(report -> {
//            MarketRankDto marketRankDto = new MarketRankDto();
//            String fundCode = report.getFundCode();
//            ProdInfoDto info = getInfo(fundCode);
//            BeanUtils.copyProperties(info, marketRankDto);
//            BeanUtils.copyProperties(report, marketRankDto);
//            marketRankDto.setPeriod(period);
//            rankDtos.add(marketRankDto);
//        });
        return rankDtos;
    }

    /**
     * 通过债基类型和时间查询市场报告
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
        }else if (TimeType.CURRENT_YEAR.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getRtnRtRank);
        }else if (TimeType.NONE.getCode().equals(period)) {
            queryWrapper.orderByAsc(BrMarketProductReport::getRtnRtRank);
        }
        List<BrMarketProductReport> brMarketProductReports = brMarketProductReportMapper.selectList(queryWrapper);
        ArrayList<MarketRankDto> rankDtos = new ArrayList<>();
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
            rankDto.setDldFlnm("全市场" + BondFundType.getBondFundTypeDesc(finBondType).getDesc() + "排名" + Year.now().getValue());
            //基金成立日
            rankDto.setContractEffDate(dealContractEffDate(rankDto.getContractEffDate()));
            //存续天数
            rankDto.setCurRenewDt(dealcurRenewDt(rankDto.getContractEffDate()));
            //债券基金类型
            rankDto.setFinBondType(BondFundType.getBondFundTypeDesc(finBondType).getDesc());
            rankDto.setPeriod(period);
            //单位净值
            rankDto.setUnitNetVal(brNetvaluesMap.get(rankDto.getFundCode()));
        });
        return rankDtos;
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
     * @return
     */
    @DS("db1")
    public String dealDldFlnm() {
        LambdaQueryWrapper<FdHoliday> queryWrapper = new LambdaQueryWrapper<FdHoliday>();
        String year = Year.now().getValue()+"";
        queryWrapper.like(FdHoliday::getThedate, year);
        queryWrapper.eq(FdHoliday::getFundworkday,"1");
        List<FdHoliday> fdHolidays = fdHolidayMapper.selectList(queryWrapper);
        return null;
    }

    public List<ProdInfoDto> getFundByType(String fundType) {
        LambdaQueryWrapper<BrProduct> queryWrapper = new LambdaQueryWrapper<BrProduct>()
                .select(BrProduct::getFundCode)
                .eq(BrProduct::getProdtClsCode, fundType);
        List<String> fundCodes = brProductMapper.selectObjs(queryWrapper).stream().map(Object::toString).toList();
        if(CollectionUtils.isEmpty(fundCodes)){
            return List.of();
        }
        // 两表联查
        return brProductMapper.selectFundDetail(fundCodes);
    }
}

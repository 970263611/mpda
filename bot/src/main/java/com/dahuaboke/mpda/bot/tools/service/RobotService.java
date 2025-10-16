package com.dahuaboke.mpda.bot.tools.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.bot.tools.dao.BrMarketProductReportMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrNetvalueMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductReportMapper;
import com.dahuaboke.mpda.bot.tools.dao.BrProductSummaryMapper;
import com.dahuaboke.mpda.bot.tools.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.tools.entity.BrMarketProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrNetvalue;
import com.dahuaboke.mpda.bot.tools.entity.BrProductReport;
import com.dahuaboke.mpda.bot.tools.entity.BrProductSummary;
import com.dahuaboke.mpda.core.utils.DateUtil;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class RobotService {

    @Autowired
    private BrProductReportMapper brProductReportMapper;

    @Autowired
    private BrProductSummaryMapper brProductSummaryMapper;

    @Autowired
    private BrNetvalueMapper brNetvalueMapper;

    @Autowired
    BrMarketProductReportMapper brMarketProductReportMapper;


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
       if(!brProductReports.isEmpty()){
           report = brProductReports.get(0);
       }
       if(!brProductSummaries.isEmpty()){
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


    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        List<BrProductReport> brProductReports = brProductReportMapper.selectList(new LambdaQueryWrapper<BrProductReport>());
        for (BrProductReport report : brProductReports) {
            map.put(report.getFundCode(), report.getFundFnm());
        }
        return map;
    }

    public List<MarketRankDto> getMarketRank(String finBondType) {
        LambdaQueryWrapper<BrMarketProductReport> queryWrapper = new LambdaQueryWrapper<BrMarketProductReport>();
        queryWrapper.eq(BrMarketProductReport::getFinBondType,finBondType)
                .orderByAsc(BrMarketProductReport::getReachStRankSeqNo)
                .last("LIMIT " + 10);

        List<BrMarketProductReport> brMarketProductReports = brMarketProductReportMapper.selectList(queryWrapper);
        ArrayList<MarketRankDto> rankDtos = new ArrayList<>();
        brMarketProductReports.forEach(report->{
            MarketRankDto marketRankDto = new MarketRankDto();
            String fundCode = report.getFundCode();
            ProdInfoDto info = getInfo(fundCode);
            BeanUtils.copyProperties(info,marketRankDto);
            BeanUtils.copyProperties(report,marketRankDto);

            rankDtos.add(marketRankDto);
        });
        return rankDtos;
    }

}

package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;

import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateStrategy implements FilterStrategy{
    private static final Logger log = LoggerFactory.getLogger(RateStrategy.class);

    @Autowired
    protected ProductToolHandler productToolHandler;

    @Override
    public List<RecommendProductDto> handle(List<RecommendProductDto> filterInfoList, FilterContext filterContext) {
        int timePeriod = filterContext.getTimePeriod();
        int totalCount = filterContext.getTotalCount();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = switch (timePeriod) {
            case 1 -> now.plusDays(-7);
            case 3 -> now.plusMonths(-3);
            case 4 -> now.plusYears(-1);
            default -> now.plusMonths(-1);
        };
        Double targetValue = filterContext.getTargetValue();
        String fundType = filterContext.getFundType();

        if (fundType.equals(FundType.MONET_MARKET_FUND.getCode())) {
            calMarketFundRate(filterInfoList, timePeriod);
            List<RecommendProductDto> recommendProductDtos = filterMarketFundRate(filterInfoList, targetValue);
            return recommendProductDtos.stream().sorted(Comparator.comparingDouble(RecommendProductDto::getSevenDayYearlyProfrat).reversed()).limit(totalCount).toList();
        }
        calRate(filterInfoList, before, now, timePeriod);
        List<RecommendProductDto> recommendProductDtos = filterRate(filterInfoList, targetValue);
        return recommendProductDtos.stream().sorted(Comparator.comparingDouble(RecommendProductDto::getRate).reversed()).limit(totalCount).toList();
    }

    private void calRate(List<RecommendProductDto> fundInfoList, LocalDateTime before, LocalDateTime now, int timePeriod) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        fundInfoList.forEach(fundInfo -> {
            try {
                String fundCode = fundInfo.getFundCode();
                NetValReq netValReq = new NetValReq(fundCode, before.format(yyyyMMdd), now.format(yyyyMMdd));
                String rate = productToolHandler.yearRita(netValReq).replace("%", "");
                double value = Double.parseDouble(rate);
                fundInfo.setRate(value);
                fundInfo.setRateTimePeriod(RecommendationTool.TimePeriod.getTimePeriodDesc(timePeriod).getDesc());
            } catch (Exception e) {
                log.error("{} 计算利率失败",fundInfo.getFundCode(),e);
            }
        });
    }

    private void calMarketFundRate(List<RecommendProductDto> fundInfoList, int timePeriod) {
        fundInfoList.forEach(fundInfo -> {
            try {
                String fundCode = fundInfo.getFundCode();
                NetValReq netValReq = new NetValReq();
                netValReq.setProdtCode(fundCode);
                String sevenDayYearlyProfrat = productToolHandler.sevenDayYearlyProfrat(netValReq);
                double value = Double.parseDouble(sevenDayYearlyProfrat);
                fundInfo.setSevenDayYearlyProfrat(value);
            } catch (Exception e) {
                log.error("{} 计算货基利率失败",fundInfo.getFundCode(),e);
            }
        });
    }

    private List<RecommendProductDto> filterRate(List<RecommendProductDto> filterInfoList, Double targetValue) {
        return filterInfoList.stream().filter(fundInfo -> {
            Double value = fundInfo.getRate();
            if(value == null){
                return false;
            }
            return value >= targetValue;
        }).toList();
    }

    private List<RecommendProductDto> filterMarketFundRate(List<RecommendProductDto> filterInfoList, Double targetValue) {
        return filterInfoList.stream().filter(fundInfo -> {
            Double value = fundInfo.getSevenDayYearlyProfrat();
            if(value == null){
                return false;
            }
            return value >= targetValue;
        }).toList();
    }

}

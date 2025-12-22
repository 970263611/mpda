package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;

import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DrawStrategy implements FilterStrategy{

    private static final Logger log = LoggerFactory.getLogger(DrawStrategy.class);

    @Autowired
    protected ProductToolHandler productToolHandler;

    @Override
    public List<RecommendProductDto> handle(List<RecommendProductDto> filterInfoList, FilterContext filterContext) {
        int timePeriod = filterContext.getTimePeriod();
        int totalCount = filterContext.getTotalCount();
        Double targetValue = filterContext.getTargetValue();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = switch (timePeriod) {
            case 1 -> now.plusDays(-7);
            case 3 -> now.plusMonths(-3);
            case 4 -> now.plusYears(-1);
            default -> now.plusMonths(-1);
        };
        calMaxWithDraw(filterInfoList,before,now,timePeriod);
        List<RecommendProductDto> recommendProductDtos = filterMaxWithDraw(filterInfoList, targetValue);
        return recommendProductDtos.stream().sorted(Comparator.comparingDouble(RecommendProductDto::getMaxWithDraw)).limit(totalCount).toList();
    }

    private void calMaxWithDraw(List<RecommendProductDto> fundInfoList, LocalDateTime before, LocalDateTime now, int timePeriod) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        fundInfoList.forEach(fundInfo -> {
            try {
                String fundCode = fundInfo.getFundCode();
                NetValReq netValReq = new NetValReq(fundCode, before.format(yyyyMMdd), now.format(yyyyMMdd));
                String draw = productToolHandler.maxWithDrawal(netValReq).replace("%", "");
                double value = Double.parseDouble(draw);
                fundInfo.setMaxWithDraw(value);
                fundInfo.setDrawTimePeriod(RecommendationTool.TimePeriod.getTimePeriodDesc(timePeriod).getDesc());
            } catch (Exception e) {
                log.error("{} 计算最大回撤失败",fundInfo.getFundCode(),e);
            }
        });
    }

    private List<RecommendProductDto> filterMaxWithDraw (List<RecommendProductDto> filterInfoList, Double targetValue){
        return filterInfoList.stream().filter(fundInfo -> {
            Double value = fundInfo.getMaxWithDraw();
            if(value == null){
                return false;
            }
            return value <= targetValue;
        }).toList();
    }

}

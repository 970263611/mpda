package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;

import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitStrategy implements FilterStrategy{

    private static final Logger log = LoggerFactory.getLogger(DrawStrategy.class);

    @Autowired
    protected ProductToolHandler productToolHandler;

    @Override
    public List<RecommendProductDto> handle(List<RecommendProductDto> filterInfoList, FilterContext filterContext) {
        String fundType = filterContext.getFundType();
        int totalCount = filterContext.getTotalCount();
        if (fundType.equals(FundType.MONET_MARKET_FUND.getCode())) {
            calUnit(filterInfoList);
            List<RecommendProductDto> recommendProductDtos = filterUnit(filterInfoList, filterContext.getTargetValue());
            return recommendProductDtos.stream().sorted(Comparator.comparingDouble(RecommendProductDto::getThouCopFundUnitProfit).reversed()).limit(totalCount).toList();
        }
        return List.of();
    }

    private void calUnit(List<RecommendProductDto> filterInfoList) {
        filterInfoList.forEach(fundInfo -> {
            try {
                String fundCode = fundInfo.getFundCode();
                NetValReq netValReq = new NetValReq();
                netValReq.setProdtCode(fundCode);
                String thouCopFundUnitProfit = productToolHandler.thouCopFundUnitProfit(netValReq);
                double value = Double.parseDouble(thouCopFundUnitProfit);
                fundInfo.setThouCopFundUnitProfit(value);
            } catch (Exception e) {
                log.error("{} 计算货基万份收益失败",fundInfo.getFundCode(),e);
            }
        });
    }


    private List<RecommendProductDto> filterUnit(List<RecommendProductDto> filterInfoList,Double targetValue) {
        return filterInfoList.stream().filter(fundInfo -> {
            Double value = fundInfo.getThouCopFundUnitProfit();
            if(value == null){
                return false;
            }
            return value >= targetValue;
        }).toList();
    }

}

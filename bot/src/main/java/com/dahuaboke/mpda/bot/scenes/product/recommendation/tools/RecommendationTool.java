package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;


import com.dahuaboke.mpda.bot.scenes.ToolCallContentParse;
import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/**
 * auth: dahua
 * time: 2025/8/22 14:39
 */
@Component
public class RecommendationTool extends ProductTool<RecommendationTool.Input> {

    private static final Logger log = LoggerFactory.getLogger(ToolCallContentParse.class);

    private static final int INDEX_TYPE = 1;

    private static final int TIME_PERIOD = 2;

    private static final int COUNT = 5;

    @Override
    public String getDescription() {
        return "根据年化利率/基金类型/月最大回撤率来查询匹配的产品信息";
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType());
    }

    @Override
    public Class<RecommendationTool.Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(RecommendationTool.Input input) {
        try {
            List<IndexTimePair> params = input.indexTimePairs;
            if (CollectionUtils.isEmpty(params)) {
                return ToolResult.error("基金类型不能为空");
            }
            log.info("recommendationTool 参数为 {}", input.indexTimePairs);

            ArrayList<RecommendProductDto> resultList = new ArrayList<>();
            Map<Integer, List<IndexTimePair>> fundParams = params.stream().collect(Collectors.groupingBy(IndexTimePair::fundTypes));
            Set<Map.Entry<Integer, List<IndexTimePair>>> entries = fundParams.entrySet();
            for (Map.Entry<Integer, List<IndexTimePair>> entry : entries) {
                int fundType = entry.getKey();
                List<RecommendProductDto> fundInfoList = productToolHandler.getFundInfoByType(String.valueOf(fundType));
                if (CollectionUtils.isEmpty(fundInfoList)) {
                    continue;
                }
                List<IndexTimePair> queryParam = entry.getValue();
                List<RecommendProductDto> prodInfoDtos = filterFundInfo(Integer.toString(fundType), fundInfoList, queryParam);
                resultList.addAll(prodInfoDtos);
            }
            return ToolResult.success("查询成功", resultList);
        } catch (RestClientException e) {
            return ToolResult.error("查询失败");
        }
    }

    private List<RecommendProductDto> filterFundInfo(String fundType, List<RecommendProductDto> fundInfoList, List<IndexTimePair> queryParam) {
        List<RecommendProductDto> filterInfoList = new ArrayList<>(fundInfoList);
        int totalCount = 0;
        int type = QueryType.DRAW.code;
        for (IndexTimePair indexTimePair : queryParam) {
            int indexType = indexTimePair.indexType == 0 ? INDEX_TYPE : indexTimePair.indexType;
            Double indexValue = indexTimePair.indexValue;
            int timePeriod = indexTimePair.timePeriod == 0 ? TIME_PERIOD : indexTimePair.timePeriod;
            totalCount += indexTimePair.count == 0 ? COUNT : indexTimePair.count;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime before = switch (timePeriod) {
                case 1 -> now.plusDays(-7);
                case 3 -> now.plusMonths(-3);
                case 4 -> now.plusYears(-1);
                default -> now.plusMonths(-1);
            };
            if (indexType == QueryType.DRAW.code) {
                filterInfoList = filterMaxWithDraw(filterInfoList, indexValue, before, now);
                continue;
            }
            if (fundType.equals(FundType.MONET_MARKET_FUND.getCode())) {
                filterInfoList = filterMarketFund(filterInfoList, indexValue);
            } else {
                filterInfoList = filterRate(filterInfoList, indexValue, before, now);
            }
            type = QueryType.RATE.code;
        }
        if (filterInfoList.isEmpty()) {
            return List.of();
        }

        if(type == QueryType.DRAW.code){
            return filterInfoList.stream().sorted(Comparator.comparingDouble(RecommendProductDto::getMaxWithDraw)).limit(Math.min(COUNT, 5)).toList();
        }
        return filterInfoList.stream().sorted(Comparator.comparingDouble(RecommendProductDto::getRate).reversed()).limit(Math.min(COUNT, 5)).toList();
    }


    private List<RecommendProductDto> filterRate(List<RecommendProductDto> fundInfoList, Double targetValue, LocalDateTime before, LocalDateTime now) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        return fundInfoList.stream().filter(fundInfo -> {
            String fundCode = fundInfo.getFundCode();
            NetValReq netValReq = new NetValReq(fundCode, before.format(yyyyMMdd), now.format(yyyyMMdd));
            String rate = productToolHandler.yearRita(netValReq).replace("%", "");
            double value = Double.parseDouble(rate);
            fundInfo.setRate(value);
            return value >= targetValue;
        }).toList();
    }

    private List<RecommendProductDto> filterMaxWithDraw(List<RecommendProductDto> fundInfoList, Double targetValue, LocalDateTime before, LocalDateTime now) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        return fundInfoList.stream().filter(fundInfo -> {
            String fundCode = fundInfo.getFundCode();
            NetValReq netValReq = new NetValReq(fundCode, before.format(yyyyMMdd), now.format(yyyyMMdd));
            String draw = productToolHandler.maxWithDrawal(netValReq).replace("%", "");
            double value = Double.parseDouble(draw);
            fundInfo.setMaxWithDraw(value);
            return value <= targetValue;
        }).toList();
    }

    private List<RecommendProductDto> filterMarketFund(List<RecommendProductDto> fundInfoList, Double targetValue) {
        return fundInfoList.stream().filter(fundInfo -> {
            String fundCode = fundInfo.getFundCode();
            NetValReq netValReq = new NetValReq();
            netValReq.setProdtCode(fundCode);
            String sevenDayYearlyProfrat = productToolHandler.sevenDayYearlyProfrat(netValReq);
            double value = Double.parseDouble(sevenDayYearlyProfrat);
            fundInfo.setRate(value);
            return value > targetValue;
        }).toList();
    }

    public enum QueryType{
        RATE(1,"收益率"),
        DRAW(2,"最大回撤");

        private final int code;

        private final String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        QueryType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    public record Input(
            @JsonPropertyDescription("""
                    基金类型-指标-时间组合列表（非必填）
                    支持1个或多个组合，每个组合独立时间
                    """) List<IndexTimePair> indexTimePairs) {

    }


    public record IndexTimePair(
            @JsonPropertyDescription("""
                    基金类型（必填）
                        用户语义有可能匹配到多个基金类型,请严格按照以下规则匹配用户语义到数字参数(1-9)
                        1：股票型基金
                        2：混合型基金
                        3：债券型基金
                        4：货币式基金
                        5：QDII
                        6：商品型基金
                        7：短期理财债券型基金
                        8：基础设施基金
                        9：基金中基金(FOF)
                    """) Integer fundTypes,
            @JsonPropertyDescription("""
                    查询指标（非必填）
                    1：收益率
                    2：最大回撤
                    """) int indexType,

            @JsonPropertyDescription("""
                    查询指标对应的值(非必填,数值类型)
                    收益率/最大回撤
                    请输入原生数值,不要自动转换百分比
                    示例:
                       用户说: "月收益率大于0.3" -> indexValue = 0.3
                       用户说: "月收益率大于30%" -> indexValue = 0.3
                    如果没有提取到,默认0.00
                    """) double indexValue,

            @JsonPropertyDescription("""
                    时间周期对应查询指标（非必填）
                    1：七日/一周/周收益/周回撤
                    2：30天/一月/月收益/月回撤
                    3：90天/三个月/三月收益/三月回撤
                    4: 360天/一年/年收益/年回撤
                    如果没有提取到,默认是2
                    """) int timePeriod,
            @JsonPropertyDescription("""
                    查询条数（非必填,默认5）
                    """) int count) {

    }

}

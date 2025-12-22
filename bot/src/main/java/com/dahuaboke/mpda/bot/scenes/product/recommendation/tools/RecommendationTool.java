package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;


import com.dahuaboke.mpda.bot.scenes.ToolCallContentParse;
import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.RecommendProductDto;
import com.dahuaboke.mpda.bot.tools.enums.BondFundType;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
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

    private static final int COUNT = 5;

    @Override
    public String getDescription() {
        return "根据基金类型/年化利率/月最大回撤率/万份收益(万份收益只针对货基类型)来查询匹配的产品信息";
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
                return ToolResult.error("查询失败", "基金类型不能是空");
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
            return ToolResult.success("查询成功", Map.of("count", resultList.size(), "result", resultList));
        } catch (RestClientException e) {
            return ToolResult.error("查询失败");
        }
    }


    private List<RecommendProductDto> filterFundInfo(String fundType, List<RecommendProductDto> fundInfoList, List<IndexTimePair> queryParam) {
        List<RecommendProductDto> filterInfoList = new ArrayList<>(fundInfoList);
        queryParam = queryParam.stream().sorted(Comparator.comparingInt(IndexTimePair::indexType).reversed()).toList();
        for (IndexTimePair indexTimePair : queryParam) {
            //查询参数
            Double indexValue = indexTimePair.indexValue;
            //查询时间范围,默认月
            int timePeriod = indexTimePair.timePeriod == 0 ? TimePeriod.MONTH.code : indexTimePair.timePeriod;
            //查询条数,默认5条
            int totalCount = indexTimePair.count == 0 ? COUNT : indexTimePair.count;
            //查询条件
            QueryType queryType = QueryType.getQueryType(indexTimePair.indexType);
            //根据查询条件,执行过滤
            FilterStrategy filterStrategy = SpringUtil.getBean(queryType.strategy);
            FilterContext filterContext = new FilterContext();
            filterContext.setTimePeriod(timePeriod);
            filterContext.setTargetValue(indexValue);
            filterContext.setFundType(fundType);
            filterContext.setTotalCount(totalCount);
            filterInfoList = filterStrategy.handle(filterInfoList,filterContext);
        }
        if (filterInfoList.isEmpty()) {
            return List.of();
        }
        return filterInfoList;
    }


    public enum QueryType {
        RATE(1, RateStrategy.class),
        DRAW(2, DrawStrategy.class),

        UNIT(3, UnitStrategy.class);

        private final int code;

        private final Class<? extends FilterStrategy> strategy;

        public int getCode() {
            return code;
        }

        public Class<? extends FilterStrategy> getStrategy() {
            return strategy;
        }

        QueryType(int code, Class<? extends FilterStrategy> strategy) {
            this.code = code;
            this.strategy = strategy;
        }
        public static QueryType getQueryType(int code){
            for (QueryType queryType : values()) {
                if (queryType.getCode()== code) {
                    return queryType;
                }
            }
            return RATE;
        }
    }


    public enum TimePeriod {

        WEEK(1, "周"),
        MONTH(2, "月"),
        QUARTER(3, "季"),
        YEAR(4, "年");
        private final int code;

        private final String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        TimePeriod(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public static TimePeriod getTimePeriodDesc(int code){
            for (TimePeriod timePeriod : values()) {
                if (timePeriod.getCode()==code) {
                    return timePeriod;
                }
            }
            return WEEK;
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
                    3：万份收益
                    """) int indexType,

            @JsonPropertyDescription("""
                    查询指标对应的值(非必填,数值类型)
                    收益率/最大回撤/万份收益
                    请输入原生数值,不要自动转换百分比
                    示例:
                       用户说: "月收益率大于0.3" -> indexValue = 0.3
                       用户说: "月收益率大于30%" -> indexValue = 30
                    如果没有提取到,默认0.00
                    """) double indexValue,

            @JsonPropertyDescription("""
                    时间周期对应查询指标（非必填）
                    1：七日/一周/周收益/周年化收益/周回撤
                    2：30天/一月/月收益/年化收益/月回撤
                    3：90天/三个月/三月收益/三月年化收益/三月回撤
                    4: 360天/一年/年收益/一年年化收益/年回撤
                    (注: 这里的"XX年化收益"等价于“XX收益” 比如"年化收益率"或者"收益率" 都代表没有指明周期,都可以看做是"月年化收益",默认匹配2)
                    如果没有提取到该参数,默认是2
                    """) int timePeriod,
            @JsonPropertyDescription("""
                    查询条数（非必填,默认5）
                    """) int count) {

    }

}

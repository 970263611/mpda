package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;


import com.dahuaboke.mpda.bot.scenes.ToolCallContentParse;
import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<IndexTimePair> params = input.indexTimePairs;
        if (CollectionUtils.isEmpty(params)) {
            return ToolResult.error("基金类型不能为空");
        }
        log.info("参数为 {}，{}", input.indexTimePairs.toString(), input.count);
        try {
            Map<String, List<IndexTimePair>> fundParams = params.stream().collect(Collectors.groupingBy(IndexTimePair::fundTypes));
            Set<Map.Entry<String, List<IndexTimePair>>> entries = fundParams.entrySet();
            for (Map.Entry<String, List<IndexTimePair>> entry: entries){
                String fundType = entry.getKey();
                List<IndexTimePair> fundParam = entry.getValue();

                //全查出来。过滤取前n条
                fundParam.forEach(indexTimePair -> {
                    String queryType = indexTimePair.indexType;
                    String timePeriod = indexTimePair.timePeriod.trim().equals("") ? indexTimePair.timePeriod.trim():"2";
                    if(fundType.equals(FundType.MONET_MARKET_FUND.getCode())){

                    }else {

                    }

                });

            }

            //2. 如果有收益率就通过当前收益率排名取前n条(取决于是否传入了count==0? 10: count)，如果没有就取一月收益率

            /*List<ProdInfoDto> prodInfoDtos = productToolHandler.filterProdInfo(new FilterProdInfoReq("input.yearRita()"
                    , fundTypes.get(0), "input.withDrawal()"));*/
            //TODO 此处为测试数据
            List<ProdInfoDto> prodInfoDtos = new ArrayList<>();
            ProdInfoDto prodInfoDto1 = new ProdInfoDto();
            prodInfoDto1.setFundCode("000730");
            ProdInfoDto prodInfoDto2 = new ProdInfoDto();
            prodInfoDto2.setFundCode("000734");
            ProdInfoDto prodInfoDto3 = new ProdInfoDto();
            prodInfoDto3.setFundCode("000758");
            prodInfoDtos.add(prodInfoDto1);
            prodInfoDtos.add(prodInfoDto2);
            prodInfoDtos.add(prodInfoDto3);
            List<Map> tempResult = new ArrayList<>();
            prodInfoDtos.stream().forEach(prodInfoDto -> {
                Map temp = new HashMap();
                temp.put("fundShortName", prodInfoDto.getFundFnm());
                temp.put("fundCode", prodInfoDto.getFundCode());
                temp.put("yearRita", getYearRita(prodInfoDto.getFundCode()));
                tempResult.add(temp);
            });

//            String nxzj = """
//                    产品名称	产品代码	产品类型	最新净值
//                    2025-8-14	近1月年化收益率	建议配置金额（亿元）
//                    农银汇理金盛	011968	主动型利率债基金	1.0165	4.28%         5亿
//                    嘉实致乾纯债	014392	主动型利率债基金	1.0533      2.86%         3亿
//                    博时锦源利率债A	020238	主动型利率债基金	1.0547      3.06%         2.5亿
//                    博时现金宝货币B	000891	货币基金	                1.0000	1.72%	  4.5亿
//                    """;
//
//
//            return ToolResult.success("查询成功", nxzj);
            if (prodInfoDtos.isEmpty()) {
                return ToolResult.error("未查询到匹配的产品信息");
            }
            return ToolResult.success("查询成功", prodInfoDtos);
        } catch (RestClientException e) {
            return ToolResult.error("查询失败");
        }
    }


    public record Input(
            @JsonPropertyDescription("""
                    基金类型-指标-时间组合列表（非必填）
                    支持1个或多个组合，每个组合独立时间
                    """) List<IndexTimePair> indexTimePairs,

            @JsonPropertyDescription("""
                    查询条数（非必填）
                    """) int count) {

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
                    """) String fundTypes,
            @JsonPropertyDescription("""
                    查询指标（非必填）
                    1：收益率
                    2：最大回撤
                    """) String indexType,
            @JsonPropertyDescription("""
                    时间周期对应查询指标（非必填,默认是2）
                    1：七日/一周
                    2：30天/一月
                    3：90天/三个月
                    4: 360天/一年
                    """) String timePeriod) {

    }

}

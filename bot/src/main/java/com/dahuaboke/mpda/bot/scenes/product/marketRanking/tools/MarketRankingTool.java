package com.dahuaboke.mpda.bot.scenes.product.marketRanking.tools;


import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:39
 */
@Component
public class MarketRankingTool extends ProductTool<MarketRankingTool.Input> {

    @Override
    public String getDescription() {
        return "查询产品市场排名";
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "productNo");
    }

    @Override
    public Class<MarketRankingTool.Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(MarketRankingTool.Input input) {
        List<MarketRankDto> marketRank = productToolHandler.getMarketRank(input.finBondType, input.period);

       /* String scpm = """
                   代码	基金	基金规模（亿元）	基金管理人	成立日期	近一年收益（%）	近一年回撤（%）	排名
                   007540.OF	华泰保兴安悦A	78.67736205	华泰保兴基金管理有限公司	2019/7/11	8.12 	-3.93 	1
                   016189.OF	国联恒通纯债A	174.5863974	国联基金管理有限公司	2022/8/8	4.45 	-0.43 	2
                   000606.OF	天弘优选A	247.5474547	天弘基金管理有限公司	2017/9/26	4.28 	-0.87 	3
                   006758.OF	农银汇理金禄	182.9556104	农银汇理基金管理有限公司	2018/12/21	3.61 	-0.89 	4
                   007492.OF	上银政策性金融债A	117.1284931	上银基金管理有限公司	2019/12/19	3.57 	-2.35 	5
                   016432.OF	财通资管睿兴A	58.45553693	财通证券资产管理有限公司	2023/4/14	3.33 	-2.17 	6
                   011968.OF	农银汇理金盛	129.4213678	农银汇理基金管理有限公司	2021/6/29	3.29 	-0.99 	7
                   010477.OF	景顺长城景泰益利A	172.8490783	景顺长城基金管理有限公司	2021/1/18	3.27 	-1.17 	8
                   016537.OF	上银慧鑫利	53.44300614	上银基金管理有限公司	2021/1/18	3.09 	-2.29 	9
                   003407.OF    景顺长城景泰丰利A   59.67640125 景顺长城基金管理有限公司    2017/1/13   3.02    -1.88   10
                """;*/
        return ToolResult.success("查询成功", marketRank);
    }

    public record Input(
            @JsonPropertyDescription("""
                    请严格按照以下规则匹配用户语义到数字参数(1-6)
                    1：信用债-指数型        -包含"信用债"且明确"指数型" "被动跟踪" 等关键字
                    2：信用债主动-开放式    -包含"信用债"且有"主动管理" "主动型" "开放式" "可申赎"等关键字
                    3：利率债主动-开放式    -包含"利率债"且有"主动管理" "主动型" "开放式" "可申赎"等关键字
                    4：利率债指数1-3年     -包含"利率债" "指数型"且明确年限"1-3年" "一年到三年"
                    5：利率债指数3-5年     -包含"利率债" "指数型"且明确年限"3-5年" "三年到五年"
                    6：利率债指数1-5年     -包含"利率债" "指数型"且明确年限"1-5年" "一年到五年"                     
                     """) String finBondType,
            @JsonPropertyDescription("""
                    请严格按照以下规则匹配用户语义到数字参数(0-9)
                    0：无
                    1：近1周
                    2：近1月
                    3：近3月
                    4：近一年
                    5：第一季度
                    6：第二季度
                    7：第三季度
                    8：第四季度
                    9：当前年                    
                     """) String period
    ) {

    }

}

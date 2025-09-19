package com.dahuaboke.mpda.bot.scenes.product.recommendation.tools;


import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:39
 */
@Component
public class RecommendationTool extends ProductTool<RecommendationTool.Input> {

    @Override
    public String getDescription() {
        return "根据年化利率/基金类型/债券基金类型/月最大回车率来查询匹配的产品信息";
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
            List<ProdInfoDto> prodInfoDtos = productToolHandler.filterProdInfo(new FilterProdInfoReq(input.yearRita()
                    , input.fundType(), input.fundClassificationCode(), input.withDrawal()));
            //TODO 此处为测试数据
            ProdInfoDto prodInfoDto1 = new ProdInfoDto();
            prodInfoDto1.setFundCode("001122");
            ProdInfoDto prodInfoDto2 = new ProdInfoDto();
            prodInfoDto2.setFundCode("002233");
            ProdInfoDto prodInfoDto3 = new ProdInfoDto();
            prodInfoDto3.setFundCode("003344");
            prodInfoDtos.add(prodInfoDto1);
            prodInfoDtos.add(prodInfoDto2);
            prodInfoDtos.add(prodInfoDto3);
            List<Map> tempResult = new ArrayList<>();
            prodInfoDtos.stream().forEach(prodInfoDto -> {
                Map temp = new HashMap();
                temp.put("fundShortName", prodInfoDto.getFundShortName());
                temp.put("fundCode", prodInfoDto.getFundCode());
                temp.put("fundManagerCompany", prodInfoDto.getFundManagerCompany());
                temp.put("netAssetValue", prodInfoDto.getNetAssetValue());
                temp.put("maxNetval", getMaxNetval(prodInfoDto.getFundCode()));
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
            return ToolResult.success("查询成功", tempResult);
        } catch (RestClientException e) {
            return ToolResult.error("查询失败");
        }
    }

    public record Input(@ToolParam(description = "年化利率（非必填）") String yearRita
            , @ToolParam(description = """
                基金类型（非必填）
                    0：货币类型基金
                    1：股票型基金
                    2：债券型基金
                    3：混合型基金
                    4：保本型基金
                    5：指数型
                    6：短债性
                    7：QDII
                此参数需要根据用户的描述匹配对应的数字，实际调用时传递的为数字
            """) List<String> fundType
            , @ToolParam(description = "债券基金类型（利率债/利率债主动-开放式）（非必填）") String fundClassificationCode
            , @ToolParam(description = "月最大回撤率（非必填）") String withDrawal) {
    }
}

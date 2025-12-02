package com.dahuaboke.mpda.bot.scenes.product.information.code.tools;

import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.bot.tools.dto.NetValReq;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.tools.enums.FundType;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class InformationByIdTool extends ProductTool<InformationByIdTool.Input> {

    @Override
    public String getDescription() {
        return """
                    通过产品编号查询产品的详细信息
                """;
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "productNo");
    }

    @Override
    public Class<Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(Input input) {
        try {
            String productNo = input.productNo();
            ProdInfoDto prodInfoDto = productToolHandler.selectProdInfo(new NetValReq(productNo));
            if (StringUtils.isEmpty(prodInfoDto.getFundCode())) {
                return ToolResult.success("查询成功", "未查询到任何消息,请确认产品编号.");
            }
            //翻译
            String prodtClsCode = prodInfoDto.getProdtClsCode();
            if (StringUtils.isNotBlank(prodtClsCode)) {
                String desc = FundType.getFundTypeDesc(prodtClsCode).getDesc();
                prodInfoDto.setProdtClsCode(desc);
            }
            Map converted = objectMapper.convertValue(prodInfoDto, Map.class);
            if (converted != null) {
                //近三月最大回撤
                converted.put("maxWithDrawal", getMaxWithDrawal(productNo));
                //近一月收益率
                converted.put("yearRita", getYearRita(productNo));
                //近三月收益率
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nowAnd3 = now.plusMonths(-3);
                converted.put("year3MRita", getYearRita(productNo, nowAnd3, now));
                //近一年收益率
                LocalDateTime nowAnd1Y = now.plusMonths(-12);
                converted.put("year1YRita", getYearRita(productNo, nowAnd1Y, now));
                //货基
                if(FundType.MONET_MARKET_FUND.getCode().equals(prodtClsCode)){
                    //7日年化
                    NetValReq netValReq = new NetValReq();
                    netValReq.setProdtCode(productNo);
                    String sevenDayYearlyProfrat = productToolHandler.sevenDayYearlyProfrat(netValReq);
                    double value = Double.parseDouble(sevenDayYearlyProfrat);
                    converted.put("year7DRita", value);
                    //TODO 万份收益


                }

            }
            return ToolResult.success("查询成功", Map.of("prodtClsCode", prodtClsCode, "result", converted));
        } catch (RestClientException e) {
            return ToolResult.error("查询失败");
        }
    }

    public record Input(@JsonPropertyDescription("产品编号(6位数字)") String productNo) {
    }
}
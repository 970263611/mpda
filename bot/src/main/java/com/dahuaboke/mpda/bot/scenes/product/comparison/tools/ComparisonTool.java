package com.dahuaboke.mpda.bot.scenes.product.comparison.tools;

import com.dahuaboke.mpda.bot.scenes.product.information.code.tools.InformationByIdTool;
import com.dahuaboke.mpda.bot.tools.ProductTool;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ComparisonTool extends ProductTool<ComparisonTool.Input> {

    @Autowired
    private InformationByIdTool informationByIdTool;

    @Override
    public String getDescription() {
        return """
                    通过两个产品的产品编号，查询两个产品信息，用于对比
                """;
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "p1", "p2");
    }

    @Override
    public Class<Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(Input input) {
        try {
            ToolResult cpxx1 = informationByIdTool.execute(new InformationByIdTool.Input(input.p1()));
            ToolResult cpxx2 = informationByIdTool.execute(new InformationByIdTool.Input(input.p2()));

            return ToolResult.success("查询成功", Map.of("first", cpxx1.getData(), "second", cpxx2.getData()));
        } catch (Exception e) {
            return ToolResult.error("查询失败");
        }
    }

    public record Input(@JsonPropertyDescription("第一个产品编号") String p1
            , @JsonPropertyDescription("第二个产品编号") String p2) {

    }

}
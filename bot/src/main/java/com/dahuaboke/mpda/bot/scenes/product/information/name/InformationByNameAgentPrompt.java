package com.dahuaboke.mpda.bot.scenes.product.information.name;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/8/22 14:10
 */
@Component
public class InformationByNameAgentPrompt extends AbstractProductAgentPrompt {

    public InformationByNameAgentPrompt(ProductToolHandler productToolHandler, ObjectMapper objectMapper) {
        Map<String, String> product = productToolHandler.getMap().entrySet().stream().collect(
                Collectors.toMap(e -> e.getKey(), e -> e.getValue().get(0)));
        String mapper = "";
        try {
            mapper = objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
        }
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，提取用户需要查询的产品名称。
                            2.产品id和产品名称的映射关系如下:
                        """
                        + mapper +

                        """
                                    3.寻找名称最匹配的10个产品
                                    4.示例：
                                        问题：帮我查询下工银的费率
                                        数据：
                                            0001：工银瑞1，
                                            0002：工银瑞2，
                                            0003：工银瑞3，
                                            0004：建银瑞4，
                                        返回：
                                            工银瑞1(0001),
                                            工银瑞2(0002),
                                            工银瑞3(0003),
                                    5.组织话语，用合适话术包装返回结果，引导用户查询返回中的产品。
                                """);
        this.description = promptMap.get("guide");
    }
}

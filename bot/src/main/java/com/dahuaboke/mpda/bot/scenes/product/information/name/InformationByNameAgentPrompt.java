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
                        结合上下文分析用户的对话内容，判断用户最近的问题是否是首次查询(首次查询可以通过用户输出的内容是否是新的产品名称),还是已确认产品,亦或是无关对话：
                           1. 首次查询(忽略历史消息,不要造成混乱)：走如下流程
                               1.1.产品id和产品名称的映射关系如下:
                                                    """
                                                    + mapper +
                                                    """
                               1.2.处理逻辑：
                                  - 匹配产品：从映射关系中寻找名称最匹配的1-10个产品(匹配的产品id必须存在于映射关系当中,如果没匹配到照样返回即可，切勿胡编乱造!)
                                  - 返回用户语义为: 为您查询到[x]支以上相关产品，请问您想了解哪支产品呢？若无您需要的产品，请您提供更详细的产品名称或产品代码，我会继续为您查询哦～
                               1.3.示例：
                                   问题：帮我查询下工银的费率
                                   数据：
                                       0001：工银瑞1，
                                       0002：工银瑞2，
                                       0003：工银瑞3，
                                       0004：建银瑞4，
                                   返回：
                                       根据你的查询，以下是与"工银"名称匹配的产品：
                                       1.工银瑞1(0001)
                                       2.工银瑞2(0002)
                                       3.工银瑞3(0003)
                                       请确认具体需要查询的产品
                           2. 已确认具体产品,此时可以通过用户上下文提取出6位产品编码,直接调用工具:informationByIdTool
                           3. 无关对话,需要引导用户补充产品名称。
                               """);
        this.description = promptMap.get("guide");
    }
}


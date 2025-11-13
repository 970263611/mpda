package com.dahuaboke.mpda.bot.scenes.product.information;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 14:02
 */
@Component
public class InformationAgentPrompt implements AgentPrompt {

    private final String prompt = """
                1.根据上下文消息和用户新问题,来判断查询产品是通过位6位产品编号还是产品名称。
                2.根据以下对应关系返回产品编号场景还是产品名称场景的对应场景编号：
                    {scenes}
                4.关键！ 必须返回{ids}的其中之一,注意不要添加任何其他符号,切勿返回其余内容影响后续流程
                5.示例：
                    数据：
                        abcdefg
                        1234567
                    返回：abcdefg
            """;
    @Autowired
    private ObjectMapper objectMapper;
    private String description;

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public void build(Map params) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate(prompt);
            Set<Map.Entry<String, String>> set = params.entrySet();
            List<String> keys = set.stream().map(Map.Entry::getKey).toList();
            promptTemplate.add("scenes", objectMapper.writeValueAsString(params));
            promptTemplate.add("ids", objectMapper.writeValueAsString(String.join("\n", keys)));
            this.description = promptTemplate.create().getContents();
        } catch (JsonProcessingException e) {
            e.printStackTrace();//TODO
        }
    }
}

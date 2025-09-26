package com.dahuaboke.mpda.requirement.scenes.resolution;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 14:02
 */
@Component
public class ResolutionAgentPrompt implements AgentPrompt {

    private final String prompt = """
                1.根据上下文和用户的问题，判断用户的聊天意向。
                2.用户的聊天意向分为以下几类：
                    {scenes}
                3.仅返回对应的意向编号，注意不要添加任何其他符号
                4.匹配意向是严谨且合理的，如果无法根据语义匹配，则返回default字符串
                5.示例：
                    数据：
                        | 编号                | 场景     |
                        | ------------------- | -------- |
                        | abcdefg-aaa-123321  | 测试场景1 |
                        | 1234567-asdfsadf-as | 测试场景2 |
                    返回编号：abcdefg-aaa-123321
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
            promptTemplate.add("scenes", objectMapper.writeValueAsString(params));
            this.description = promptTemplate.create().getContents();
        } catch (JsonProcessingException e) {
            e.printStackTrace();//TODO
        }
    }
}

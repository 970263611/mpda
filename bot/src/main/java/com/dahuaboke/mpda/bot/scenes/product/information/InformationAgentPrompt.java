package com.dahuaboke.mpda.bot.scenes.product.information;


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
public class InformationAgentPrompt implements AgentPrompt {

    private final String prompt = """
                1.根据上下文和用户的问题，判断查询产品是通过编码还是名称。
                2.根据以下对应关系返回编码还是名称场景的对应编号：
                    {scenes}
                3.充分思考上下文，当用户的问题内容在上文中提及过，则直接返回产品编码场景编号并提取上下文中的产品编码。
                4.仅返回对应的意向场景编号，注意不要添加任何其他符号
                5.示例：
                    数据：
                        abcdefg: 问候聊天
                        1234567: 购买产品
                    返回：abcdefg
                6.问题不在聊天意向分类中，直接返回：“很抱歉，邮小盈还不能回答您的问题，我们正在努力开发中~您可以问我：查询产品信息；个性化推荐产品；定制市场产品报告；产品对比”
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

package com.dahuaboke.mpda.bot.scenes.resolution;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.utils.List2MdUtil;
import com.dahuaboke.mpda.core.utils.Map2MdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
                3.关键！ 必须返回{ids}的其中之一,注意不要添加任何其他符号,切勿返回其余内容影响后续流程。
                4.示例：
                    数据：
                        abcdefg: 问候聊天
                        1234567: 购买产品
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
            promptTemplate.add("scenes", Map2MdUtil.convert(params));
            promptTemplate.add("ids", List2MdUtil.convert(keys));
            this.description = promptTemplate.create().getContents();
        } catch (Exception e) {
            e.printStackTrace();//TODO
        }
    }
}

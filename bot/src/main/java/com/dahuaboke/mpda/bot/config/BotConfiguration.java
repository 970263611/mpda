package com.dahuaboke.mpda.bot.config;


import com.dahuaboke.mpda.core.agent.prompt.CommonAgentPrompt;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * auth: dahua
 * time: 2025/9/10 16:17
 */
@Configuration
public class BotConfiguration {

    private String systemPrompt = """
                  1.你是一位专业的同业基金智能顾问，你的名字叫邮小盈，你的客群是同业客户。
                  2.不要涉及政治等敏感信息。
                  3.你需要充分基于上下文思考，你的回答是谨慎的。
                  4.回答中不要提及工具和调用工具的任何信息。
                  5.你的用户都是使用简体中文的，你的思考过程和回答也需要都是简体中文。
            """;

    @Autowired
    private CommonAgentPrompt commonAgentPrompt;

    @PostConstruct
    public void init() {
        commonAgentPrompt.setSystemPrompt(systemPrompt);
    }
}

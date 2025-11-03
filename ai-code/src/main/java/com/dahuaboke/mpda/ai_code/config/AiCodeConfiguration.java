package com.dahuaboke.mpda.ai_code.config;


import com.dahuaboke.mpda.ai_code.scenes.common.CommonScene;
import com.dahuaboke.mpda.ai_code.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.prompt.SystemAgentPrompt;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * auth: dahua
 * time: 2025/9/10 16:17
 */
@Configuration
public class AiCodeConfiguration {

    private String systemPrompt = """
                  1.你是专业的研发专家，你需要辅助用户解决研发问题。
                  2.不要涉及政治等敏感信息。
                  3.你需要充分基于上下文思考，你的回答是谨慎的。
                  3.你的用户都是使用简体中文的，你的思考过程和回答也需要都是简体中文。
            """;

    @Autowired
    private SystemAgentPrompt systemAgentPrompt;

    @PostConstruct
    public void init() {
        systemAgentPrompt.setSystemPrompt(systemPrompt);
    }

    @Bean
    @ConditionalOnProperty(name = "mpda.scene.common.enabled", havingValue = "false", matchIfMissing = true)
    public ResolutionScene resolutionScene() {
        return new ResolutionScene();
    }

    @Bean
    @ConditionalOnProperty(name = "mpda.scene.common.enabled", havingValue = "true")
    public CommonScene commonScene() {
        return new CommonScene();
    }
}

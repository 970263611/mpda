package com.dahuaboke.mpda.ai_code.scenes.resolution;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 14:02
 */
@Component
public class ResolutionAgentPrompt implements AgentPrompt {

    private final String prompt = "";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CacheManager cacheManager;
    private String description;

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public void build(Map params) {
        this.description = buildPrompt(prompt, params);
        String promptFromFie = cacheManager.getPromptFromFie();
        cacheManager.setPromptFromFie(buildPrompt(promptFromFie, params));
    }

    private String buildPrompt(String prompt, Map params) {
        try {
            if (StringUtils.hasLength(prompt)) {
                PromptTemplate promptTemplate = new PromptTemplate(prompt);
                promptTemplate.add("scenes", objectMapper.writeValueAsString(params));
                return promptTemplate.create().getContents();
            }
        } catch (JsonProcessingException e) {
        }
        return new String();
    }
}

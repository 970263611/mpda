package com.dahuaboke.mpda.ai_code.scenes.common;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/11/3 15:15
 */
@Component
public class CommonAgentPrompt implements AgentPrompt {

    private final String prompt = "";

    @Override
    public String description() {
        return this.prompt;
    }

    @Override
    public void build(Map params) {
    }
}


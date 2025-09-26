package com.dahuaboke.mpda.requirement.scenes.requirement.optimize;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/26 11:17
 */
@Component
public class OptimizeAgentPrompt implements AgentPrompt {

    private final String prompt = "提示词";

    @Override
    public String description() {
        return this.prompt;
    }

    @Override
    public void build(Map params) {
    }
}


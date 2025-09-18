package com.dahuaboke.mpda.core.agent.prompt;


import com.dahuaboke.mpda.core.exception.MpdaInvocationException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 10:21
 */
@Component
public class CommonAgentPrompt implements AgentPrompt {

    private String systemPrompt = "";

    @Override
    public String description() {
        throw new MpdaInvocationException();
    }

    @Override
    public void build(Map params) {

    }

    public String system() {
        return systemPrompt;
    }

    public void setSystemPrompt(String prompt) {
        this.systemPrompt = prompt;
    }
}

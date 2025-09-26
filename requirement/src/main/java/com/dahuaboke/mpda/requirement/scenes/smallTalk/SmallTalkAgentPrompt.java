package com.dahuaboke.mpda.requirement.scenes.smallTalk;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 09:17
 */
@Component
public class SmallTalkAgentPrompt implements AgentPrompt {

    public final String description = """
                1.表明自己的身份与工作职责
                2.友好礼貌的回应客户
            """;

    @Override
    public String description() {
        return description;
    }

    @Override
    public void build(Map params) {

    }
}

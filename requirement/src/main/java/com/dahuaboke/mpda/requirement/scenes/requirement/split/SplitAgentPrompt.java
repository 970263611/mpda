package com.dahuaboke.mpda.requirement.scenes.requirement.split;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/26 11:16
 */
@Component
public class SplitAgentPrompt implements AgentPrompt {

    private final String prompt = "请根据多个文件的内容，帮我进行需求拆分，变成多个需求条目，每个需求条目包含需求名称和需求描述。";

    @Override
    public String description() {
        return this.prompt;
    }

    @Override
    public void build(Map params) {
    }
}


package com.dahuaboke.mpda.ai_code.scenes.common;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * auth: dahua
 * time: 2025/11/3 15:15
 */
public class CommonScene implements Scene {

    @Autowired
    private CommonGraph commonGraph;

    @Autowired
    private CommonAgentPrompt commonAgentPrompt;

    @Override
    public String description() {
        return "通用处理场景";
    }

    @Override
    public Graph graph() {
        return commonGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return commonAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return null;
    }
}


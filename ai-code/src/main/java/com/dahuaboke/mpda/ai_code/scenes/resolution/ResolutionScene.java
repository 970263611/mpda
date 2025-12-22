package com.dahuaboke.mpda.ai_code.scenes.resolution;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/8/21 15:03
 */
public class ResolutionScene implements Scene {

    @Autowired
    private ResolutionGraph resolutionGraph;

    @Autowired
    private ResolutionAgentPrompt resolutionPrompt;

    @Override
    public String description() {
        return "场景分发";
    }

    @Override
    public Graph graph() {
        return resolutionGraph;
    }

    @Override
    public List<AgentPrompt> prompt() {
        return List.of(resolutionPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return null;
    }
}

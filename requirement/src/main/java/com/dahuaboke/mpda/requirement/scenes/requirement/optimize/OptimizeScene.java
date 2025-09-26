package com.dahuaboke.mpda.requirement.scenes.requirement.optimize;


import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/26 11:17
 */
@Component
public class OptimizeScene implements Scene {

    @Autowired
    private OptimizeGraph optimizeGraph;

    @Autowired
    private OptimizeAgentPrompt optimizeAgentPrompt;

    @Override
    public String description() {
        return "描述";
    }

    @Override
    public Graph graph() {
        return optimizeGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return optimizeAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}


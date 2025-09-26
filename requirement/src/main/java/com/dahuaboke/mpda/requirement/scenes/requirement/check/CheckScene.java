package com.dahuaboke.mpda.requirement.scenes.requirement.check;


import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/26 11:14
 */
@Component
public class CheckScene implements Scene {

    @Autowired
    private CheckGraph checkGraph;

    @Autowired
    private CheckAgentPrompt checkAgentPrompt;

    @Override
    public String description() {
        return "描述";
    }

    @Override
    public Graph graph() {
        return checkGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return checkAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}


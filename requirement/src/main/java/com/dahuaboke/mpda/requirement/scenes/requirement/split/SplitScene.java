package com.dahuaboke.mpda.requirement.scenes.requirement.split;


import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/26 11:16
 */
@Component
public class SplitScene implements Scene {

    @Autowired
    private SplitGraph splitGraph;

    @Autowired
    private SplitAgentPrompt splitAgentPrompt;

    @Override
    public String description() {
        return "描述";
    }

    @Override
    public Graph graph() {
        return splitGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return splitAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}


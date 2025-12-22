package com.dahuaboke.mpda.requirement.scenes.requirement.value;


import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/10/17 9:46
 */
@Component
public class ValueScene implements Scene {

    @Autowired
    private ValueGraph valueGraph;

    @Autowired
    private ValueAgentPrompt valueAgentPrompt;

    @Override
    public String description() {
        return "描述";
    }

    @Override
    public Graph graph() {
        return valueGraph;
    }

    @Override
    public List<AgentPrompt> prompt() {
        return List.of(valueAgentPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}


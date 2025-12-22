package com.dahuaboke.mpda.requirement.scenes.requirement.document;


import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/9/26 11:16
 */
@Component
public class DocumentScene implements Scene {

    @Autowired
    private DocumentGraph documentGraph;

    @Autowired
    private DocumentAgentPrompt documentAgentPrompt;

    @Override
    public String description() {
        return "描述";
    }

    @Override
    public Graph graph() {
        return documentGraph;
    }

    @Override
    public List<AgentPrompt> prompt() {
        return List.of(documentAgentPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}


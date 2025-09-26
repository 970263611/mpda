package com.dahuaboke.mpda.requirement.scenes.requirement.analyze;


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
public class AnalyzeScene implements Scene {

    @Autowired
    private AnalyzeGraph analyzeGraph;

    @Autowired
    private AnalyzeAgentPrompt analyzeAgentPrompt;

    @Override
    public String description() {
        return "描述";
    }

    @Override
    public Graph graph() {
        return analyzeGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return analyzeAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}


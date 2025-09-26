package com.dahuaboke.mpda.requirement.scenes.codeGenerate;


import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/10 15:58
 */
@Component
public class CodeGenerateScene implements Scene {

    @Autowired
    private CodeGenerateGraph codeGenerateGraph;

    @Autowired
    private CodeGenerateAgentPrompt codeGenerateAgentPrompt;

    @Override
    public String description() {
        return "生成代码";
    }

    @Override
    public Graph graph() {
        return codeGenerateGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return codeGenerateAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

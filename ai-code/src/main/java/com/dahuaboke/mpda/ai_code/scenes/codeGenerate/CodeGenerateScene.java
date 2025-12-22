package com.dahuaboke.mpda.ai_code.scenes.codeGenerate;


import com.dahuaboke.mpda.ai_code.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public List<AgentPrompt> prompt() {
        return List.of(codeGenerateAgentPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

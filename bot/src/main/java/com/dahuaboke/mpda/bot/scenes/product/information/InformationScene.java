package com.dahuaboke.mpda.bot.scenes.product.information;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/10/28 10:12
 */
@Component
public class InformationScene implements Scene {

    @Autowired
    private InformationGraph informationGraph;

    @Autowired
    private InformationAgentPrompt informationAgentPrompt;

    @Override
    public String description() {
        return """
                    了解/查询/咨询/介绍基金具体/某一/部分信息
                    了解/查询/咨询/介绍产品具体/某一/部分信息
                """;
    }

    @Override
    public Graph graph() {
        return informationGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return informationAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

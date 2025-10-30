package com.dahuaboke.mpda.bot.scenes.product.information.name;


import com.dahuaboke.mpda.bot.scenes.product.information.InformationScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:10
 */
@Component
public class InformationByNameScene implements Scene {

    private final String description = """
                通过产品名称或产品编码查询产品具体信息
                通过产品名称或产品编码介绍产品
                通过产品名称或产品编码了解产品详情
            """;

    @Autowired
    private InformationByNameGraph informationByNameGraph;

    @Autowired
    private InformationByNameAgentPrompt informationPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return informationByNameGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return informationPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return InformationScene.class;
    }
}

package com.dahuaboke.mpda.bot.scenes.product.information.unknow;


import com.dahuaboke.mpda.bot.scenes.product.information.InformationScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/11/25 13:52
 */
@Component
public class InformationUnknownScene implements Scene {

    @Autowired
    private InformationUnknownGraph informationUnknownGraph;

    @Override
    public String description() {
        return "没有提供编号或名称";
    }

    @Override
    public Graph graph() {
        return informationUnknownGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return null;
    }

    @Override
    public Class<? extends Scene> parent() {
        return InformationScene.class;
    }
}

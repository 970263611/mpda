package com.dahuaboke.mpda.bot.scenes.product.recommendation;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:47
 */
@Component
public class RecommendationScene implements Scene {

    @Autowired
    private RecommendationGraph recommendationGraph;

    @Autowired
    private RecommendationAgentPrompt recommendationPrompt;

    @Override
    public String description() {
        return """
                    根据基金类型/年化利率/最大回撤指标 为用户推荐产品
                    根据基金类型/年化利率/最大回撤指标 为用户配置产品
                """;
    }

    @Override
    public Graph graph() {
        return recommendationGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return recommendationPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }

}

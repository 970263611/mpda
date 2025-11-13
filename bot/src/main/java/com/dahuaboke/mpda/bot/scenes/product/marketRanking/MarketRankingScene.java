package com.dahuaboke.mpda.bot.scenes.product.marketRanking;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingScene implements Scene {

    private final String description = "查询产品市场排名/定制市场分析报告";

    @Autowired
    private MarketRankingGraph marketRankingGraph;

    @Autowired
    private MarketRankingAgentPrompt marketRankingPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return marketRankingGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return marketRankingPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

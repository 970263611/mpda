package com.dahuaboke.mpda.bot.scenes.product.marketRanking;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.memory.MemoryExclude;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
@MemoryExclude(ToolResponseMessage.class)
public class MarketRankingScene implements Scene {

    private final String description = """
                定制市场分析报告
                查询产品市场排名
                根据基金类型和时间范围 定制市场分析报告
            """;

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
    public List<AgentPrompt> prompt() {
        return List.of(marketRankingPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

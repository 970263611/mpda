package com.dahuaboke.mpda.bot.scenes.product.comparison;


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
 * time: 2025/8/22 09:13
 */
@Component
@MemoryExclude(ToolResponseMessage.class)
public class ComparisonScene implements Scene {

    private final String description = """
                基金产品对比
                对比量产品
            """;

    @Autowired
    private ComparisonGraph comparisonGraph;

    @Autowired
    private ComparisonAgentPrompt comparisonPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return comparisonGraph;
    }

    @Override
    public List<AgentPrompt> prompt() {
        return List.of(comparisonPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

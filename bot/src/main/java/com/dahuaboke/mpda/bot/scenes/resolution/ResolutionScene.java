package com.dahuaboke.mpda.bot.scenes.resolution;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.memory.MemoryExclude;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/21 15:03
 */
@Component
@MemoryExclude(ToolResponseMessage.class)
public class ResolutionScene implements Scene {

    @Autowired
    @Qualifier("resolutionGraph")
    private ResolutionGraph resolutionGraph;

    @Autowired
    @Qualifier("resolutionAgentPrompt")
    private ResolutionAgentPrompt resolutionPrompt;

    @Override
    public String description() {
        return "场景分发";
    }

    @Override
    public Graph graph() {
        return resolutionGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return resolutionPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return null;
    }
}

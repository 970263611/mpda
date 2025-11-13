package com.dahuaboke.mpda.bot.scenes.smallTalk;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.memory.MemoryExclude;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/21 15:03
 */
@Component
@MemoryExclude(ToolResponseMessage.class)
public class SmallTalkScene implements Scene {

    private final String description = "问候聊天";

    @Autowired
    private SmallTalkGraph smallTalkGraph;

    @Autowired
    private SmallTalkAgentPrompt smallTalkPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return smallTalkGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return smallTalkPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

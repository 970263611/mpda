package com.dahuaboke.mpda.requirement.scenes.smallTalk;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.requirement.scenes.resolution.ResolutionScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/8/21 15:03
 */
@Component
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
    public List<AgentPrompt> prompt() {
        return List.of(smallTalkPrompt);
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}

package com.dahuaboke.mpda.core.agent.scene;

import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/08/21 8:50
 */
public interface Scene {

    String description();

    Graph graph();

    List<AgentPrompt> prompt();

    Class<? extends Scene> parent();
}

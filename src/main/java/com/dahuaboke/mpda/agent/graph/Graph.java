package com.dahuaboke.mpda.agent.graph;


import com.dahuaboke.mpda.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.agent.exception.MpdaRuntimeException;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/8/21 09:12
 */
public interface Graph {

    void init(Set<String> keys) throws MpdaGraphException;

    String execute(Map<String, Object> attribute) throws MpdaRuntimeException;

    Flux<String> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException;

    void addMemory(Message message);

    void addMemory(String conversationId, String sceneId, Message message);
}

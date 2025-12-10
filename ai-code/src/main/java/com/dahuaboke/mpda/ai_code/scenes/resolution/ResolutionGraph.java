package com.dahuaboke.mpda.ai_code.scenes.resolution;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.event.EventPublisher;
import com.dahuaboke.mpda.core.event.monitor.MessageChangeEvent;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.MessageWrapper;
import com.dahuaboke.mpda.core.node.LlmNode;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;
import static com.dahuaboke.mpda.core.event.Event.Type.ADDED;

/**
 * auth: dahua
 * time: 2025/8/21 09:12
 */
@Component
public class ResolutionGraph extends AbstractGraph {

    @Autowired
    private LlmNode llmNode;
    @Autowired
    private EventPublisher eventPublisher;

    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))

                    .addEdge(StateGraph.START, "llm")
                    .addEdge("llm", StateGraph.END);
            return Map.of("default", stateGraph);
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return response(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return streamResponse(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public void addMemory(Message message) {
        if (message instanceof MessageWrapper messageWrapper) {
            eventPublisher.publish(new MessageChangeEvent(messageWrapper, ADDED));
        }
    }

    @Override
    public void addMemory(String conversationId, String sceneId, Message message) {
    }
}

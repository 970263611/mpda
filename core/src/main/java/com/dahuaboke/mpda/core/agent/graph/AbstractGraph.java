package com.dahuaboke.mpda.core.agent.graph;


import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneExtend;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.client.entity.EmbeddingResponse;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.serializer.CustomSpringAIJacksonStateSerializer;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/8/21 14:13
 */
public abstract class AbstractGraph implements Graph {

    private final Map<Object, CompiledGraph> graphs = new HashMap<>();
    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    protected MemoryManager memoryManager;

    @Override
    public void init(Set<String> keys) throws MpdaGraphException {
        KeyStrategyFactory keyStrategyFactory = buildKeyStrategyFactory(keys);
        Map<Object, StateGraph> graphMap = buildGraph(keyStrategyFactory);
        graphMap.forEach((k, v) -> {
            try {
                Class<? extends StateGraph> graphClass = v.getClass();
                Field stateSerializer = graphClass.getDeclaredField("stateSerializer");
                stateSerializer.setAccessible(true);
                stateSerializer.set(v, new CustomSpringAIJacksonStateSerializer(OverAllState::new));
                graphs.put(k, v.compile());
            } catch (GraphStateException e) {
                throw new MpdaRuntimeException("Compiled graph failed.", e);
            } catch (NoSuchFieldException e) {
                throw new MpdaRuntimeException("Compiled graph failed.", e);
            } catch (IllegalAccessException e) {
                throw new MpdaRuntimeException("Compiled graph failed.", e);
            }
        });
    }

    protected KeyStrategyFactory buildKeyStrategyFactory(Set<String> keys) {
        return () -> {
            Map<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
            keys.stream().forEach(key -> keyStrategyHashMap.put(key, new ReplaceStrategy()));
            return keyStrategyHashMap;
        };
    }

    @Override
    public void addMemory(Message message) {
        memoryManager.addMemory(message);
    }

    @Override
    public void addMemory(String conversationId, String sceneName, Message message) {
        memoryManager.addMemory(conversationId, sceneName, message);
    }

    abstract public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException;

    private CompiledGraph getGraph(Object key) {
        CompiledGraph compiledGraph = graphs.get(key);
        if (compiledGraph == null) {
            throw new MpdaIllegalArgumentException(key.toString());
        }
        return compiledGraph;
    }

    protected SceneResponse response(Map<String, Object> attribute, String graphKey) throws GraphRunnerException {
        OverAllState overAllState = getGraph(graphKey).invoke(attribute).get();
        Object result = overAllState.value(Constants.RESULT).get();
        String text = null;
        if (result != null) {
            if (result instanceof LlmResponse llmResponse) {
                text = llmResponse.chatResponse().getResult().getOutput().getText();
            } else if (result instanceof EmbeddingResponse embeddingResponse) {
                text = embeddingResponse.obj().toString();
            }
        }
        List<Object> toolExtend = overAllState.value(Constants.EXTEND, List.class).orElse(null);
        return new SceneResponse(text, buildExtend(toolExtend));
    }

    protected Flux<SceneResponse> streamResponse(Map<String, Object> attribute, String graphKey) throws GraphRunnerException {
        return getGraph(graphKey).stream(attribute)
                .map(output -> {
                    if (output instanceof StreamingOutput<?> streamingOutput) {
                        return new SceneResponse(streamingOutput.chunk(), null);
                    } else {
                        OverAllState overAllState = output.state();
                        List<Object> toolExtend = overAllState.value(Constants.EXTEND, List.class).orElse(null);
                        return new SceneResponse("", buildExtend(toolExtend));
                    }
                }).share();
    }

    public SceneExtend buildExtend(List<Object> toolExtend) {
        return new SceneExtend(toolExtend);
    }
}

package com.dahuaboke.mpda.core.agent.graph;


import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneExtend;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
                graphs.put(k, v.compile());
            } catch (GraphStateException e) {
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
    public void addMemory(String conversationId, String sceneId, Message message) {
        memoryManager.addMemory(conversationId, sceneId, message);
    }

    abstract public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException;

    private Flux<String> changeFlux(AsyncGenerator<NodeOutput> generator) {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        CompletableFuture.runAsync(() -> generator.forEachAsync(output -> {
            try {
                if (output instanceof StreamingOutput streamingOutput) {
                    sink.tryEmitNext(streamingOutput.chunk());
                } else {
                    OverAllState state = output.state();

                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenRun(() -> sink.tryEmitComplete()).exceptionally(ex -> {
            sink.tryEmitError(ex);
            return null;
        }));
        return sink.asFlux() // TODO
                .doOnCancel(() -> System.out.println("Client disconnected from stream"))
                .doOnError(e -> System.err.println("Error occurred during streaming: " + e));
    }

    private CompiledGraph getGraph(Object key) {
        CompiledGraph compiledGraph = graphs.get(key);
        if (compiledGraph == null) {
            throw new MpdaIllegalArgumentException(key.toString());
        }
        return compiledGraph;
    }

    protected SceneResponse response(Map<String, Object> attribute, String graphKey) throws GraphRunnerException {
        return response(attribute, graphKey, null);
    }

    protected SceneResponse response(Map<String, Object> attribute, String graphKey, Object graphExtend) throws GraphRunnerException {
        OverAllState overAllState = getGraph(graphKey).invoke(attribute).get();
        LlmResponse llmResponse = overAllState.value(Constants.RESULT, LlmResponse.class).get();
        String text = llmResponse.chatResponse().getResult().getOutput().getText();
        Object toolExtend = overAllState.value(Constants.EXTEND).orElse(null);
        return new SceneResponse(text, buildSceneExtend(graphExtend,toolExtend));
    }

    protected Flux<SceneResponse> streamResponse(Map<String, Object> attribute, String graphKey) throws GraphRunnerException {
        return streamResponse(attribute, graphKey, null);
    }

    protected Flux<SceneResponse> streamResponse(Map<String, Object> attribute, String graphKey, Object graphExtend) throws GraphRunnerException {
        AsyncGenerator<NodeOutput> generator = getGraph(graphKey).stream(attribute,
                RunnableConfig.builder().threadId(cacheManager.getContext().getSceneId()).build());
        Sinks.Many<SceneResponse> sink = Sinks.many().multicast().onBackpressureBuffer();
        CompletableFuture.runAsync(() -> generator.forEachAsync(output -> {
            try {
                if (output instanceof StreamingOutput streamingOutput) {
                    sink.tryEmitNext(new SceneResponse(streamingOutput.chunk(), null));
                } else {
                    OverAllState overAllState = output.state();
                    Object toolExtend = overAllState.value(Constants.EXTEND).orElse(null);
                    sink.tryEmitNext(new SceneResponse("", buildSceneExtend(graphExtend,toolExtend)));
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenRun(() -> sink.tryEmitComplete()).exceptionally(ex -> {
            sink.tryEmitError(ex);
            return null;
        }));
        return sink.asFlux() // TODO
                .doOnCancel(() -> System.out.println("Client disconnected from stream"))
                .doOnError(e -> System.err.println("Error occurred during streaming: " + e));
    }

    public SceneExtend buildSceneExtend(Object graphExtend, Object toolExtend) {
        return new SceneExtend(graphExtend,toolExtend);
    };
}

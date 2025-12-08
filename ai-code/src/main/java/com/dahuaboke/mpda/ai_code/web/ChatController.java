package com.dahuaboke.mpda.ai_code.web;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.AppendStrategy;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.dahuaboke.mpda.core.serializer.CustomSpringAIJacksonStateSerializer;
import com.dahuaboke.mpda.ai_code.web.service.ChatService;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;

@CrossOrigin
@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> chat(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException {
        CoreContext context = new CoreContext(q, conversationId);
        Flux<SceneResponse> response = chatService.chat(context);
        return response.map(res -> {
            Map<String, Object> delta = Map.of("role", "assistant", "content", res.output());
            Map<String, Object> choice = Map.of("index", 0, "delta", delta, "finish_reason", "");
            List<Map<String, Object>> choices = List.of(choice);
            try {
                String jsonData = objectMapper.writeValueAsString(Map.of("choices", choices));
                return ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event("message")
                        .data(jsonData)
                        .build();
            } catch (JsonProcessingException e) {
                return ServerSentEvent.<String>builder().build();
            }
        });
    }

    @RequestMapping("/stream1")
    public void test() throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> {
            Map<String, KeyStrategy> keyStrategyMap = new HashMap<>();
            keyStrategyMap.put("query", new AppendStrategy());
            keyStrategyMap.put("messages", new AppendStrategy());
            keyStrategyMap.put("result", new AppendStrategy());
            return keyStrategyMap;
        };

        StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                .addNode("streaming_node", AsyncNodeAction.node_async(state -> {
                    return Map.of("a", Flux.interval(Duration.ofMillis(100)).take(10).map(l ->
                            new ChatResponse(List.of(new Generation(new AssistantMessage(String.valueOf(l)))))));
                }))
                .addEdge(START, "streaming_node")
                .addEdge("streaming_node", END);

        var graph = stateGraph.compile();

        graph.stream(Map.of("query", "请用一句话介绍 Spring AI"))
                .doOnNext(output -> {
                    // 处理流式输出
                    if (output instanceof StreamingOutput<?> streamingOutput) {
                        // 流式输出块
                        String chunk = streamingOutput.chunk();
                        if (chunk != null && !chunk.isEmpty()) {
                            System.out.println(chunk); // 实时打印流式内容
                        }
                    } else {
                        // 普通节点输出
                        String nodeId = output.node();
                        Map<String, Object> state = output.state().data();
                        if (state.containsKey("result")) {
                            System.out.println("最终结果: " + state.get("result"));
                        }
                    }
                })
                .doOnComplete(() -> {
                    System.out.println("success");
                })
                .doOnError(error -> {
                    System.err.println("流式输出错误: " + error.getMessage());
                })
                .blockLast(); // 阻塞等待流完成
    }

    @RequestMapping("/stream2")
    public void test2() throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> {
            Map<String, KeyStrategy> keyStrategyMap = new HashMap<>();
            keyStrategyMap.put("query", new AppendStrategy());
            keyStrategyMap.put("messages", new AppendStrategy());
            keyStrategyMap.put("result", new AppendStrategy());
            return keyStrategyMap;
        };

        StateGraph stateGraph = new StateGraph(keyStrategyFactory,new CustomSpringAIJacksonStateSerializer(OverAllState::new))
                .addNode("streaming_node", AsyncNodeAction.node_async(state -> {
                    return Map.of("a", new ChatResponse(List.of(new Generation(new AssistantMessage("asdfasdf")))));
                }))
                .addEdge(START, "streaming_node")
                .addEdge("streaming_node", END);

        var graph = stateGraph.compile();

        System.out.println(graph.invoke(Map.of()).get().value("a").get());
    }
}

package com.dahuaboke.mpda.core.client;


import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.streaming.StreamingChatGenerator;
import com.dahuaboke.mpda.core.agent.prompt.CommonAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.client.entity.StreamLlmResponse;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import com.dahuaboke.mpda.core.memory.UserMessageWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * auth: dahua
 * time: 2025/8/21 10:20
 */
public class ChatClientManager {

    private final ChatClient chatClient;
    private final MemoryManager memoryManager;

    public ChatClientManager(ChatModel chatModel, CommonAgentPrompt commonPrompt, MemoryManager memoryManager, ToolCallbackProvider tools) {
        this.memoryManager = memoryManager;
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem(commonPrompt.system())
                .defaultToolCallbacks(tools)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    public LlmResponse call(String conversationId, String sceneId, String prompt, Object query, List<ToolCallback> tools, List<String> sceneMerge, Boolean isToolQuery) {
        ChatClient.ChatClientRequestSpec spec = buildChatClientRequestSpec(conversationId, sceneId, prompt, query, tools, sceneMerge, isToolQuery);
        ChatResponse chatResponse = spec.call().chatResponse();
        return new LlmResponse(chatResponse);
    }

    public StreamLlmResponse stream(String conversationId, String sceneId, String prompt, Object query, String key
            , OverAllState state, String nodeName, List<String> sceneMerge, Boolean isToolQuery) {
        ChatClient.ChatClientRequestSpec spec = buildChatClientRequestSpec(conversationId, sceneId, prompt, query, null, sceneMerge, isToolQuery);
        Flux<ChatResponse> chatResponseFlux = spec.stream().chatResponse();


        AsyncGenerator<? extends NodeOutput> output = StreamingChatGenerator.builder()
                .startingNode(nodeName)
                .startingState(state)
                .mapResult(response -> Map.of(key
                        , Objects.requireNonNull(response.getResult().getOutput().getText())))
                .build(chatResponseFlux);

        return new StreamLlmResponse(output);
    }

    private ChatClient.ChatClientRequestSpec buildChatClientRequestSpec(String conversationId, String sceneId, String prompt
            , Object query, List<ToolCallback> tools, List<String> sceneMerge, Boolean isToolQuery) {
        ChatClient.ChatClientRequestSpec spec = chatClient.prompt(prompt);
        List<Message> messages = memoryManager.getMemory(conversationId, sceneId, sceneMerge);
        if (CollectionUtils.isEmpty(messages)) {
            messages = List.of();
        }
        Message message;
        if (isToolQuery) {
            message = (ToolResponseMessageWrapper) query;
        } else {
            message = UserMessageWrapper.builder().text((String) query).build();
        }
        List<Message> finalMessages = new ArrayList<>(messages);
        finalMessages.add(message);
        spec.messages(finalMessages);
        if (!CollectionUtils.isEmpty(tools)) {
            spec.toolCallbacks(tools);
        }
        return spec;
    }

}

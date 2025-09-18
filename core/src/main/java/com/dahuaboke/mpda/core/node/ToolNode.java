package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.agent.tools.ToolUtil;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.memory.AssistantMessageWrapper;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * auth: dahua
 * time: 2025/8/22 10:24
 */
@Component
public class ToolNode implements NodeAction {

    @Autowired
    private ToolUtil toolUtil;

    @Autowired
    private MemoryManager memoryManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        ChatResponse chatResponse = chatResponse(state);
        ToolResponseMessage toolResponseMessage = executeTool(chatResponse);

        ToolResponseMessageWrapper toolResponseMessageWrapper = buildToolResponseMessageWrapper(state, toolResponseMessage);
        Map<String,Object> apply = new HashMap<>() {{
            put(Constants.QUERY, toolResponseMessageWrapper);
            put(Constants.IS_TOOL_QUERY, true);
        }};
        HashMap<String, Object> toolInputArguments = getToolInputArguments(chatResponse);
        if (!toolInputArguments.isEmpty()) {
            apply.put(Constants.EXTEND, toolInputArguments);
        }
        return apply;
    }

    protected ChatResponse chatResponse(OverAllState state) {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        AssistantMessageWrapper assistantMessageWrapper =
                new AssistantMessageWrapper(assistantMessage.getText(), assistantMessage.getMetadata(), assistantMessage.getToolCalls(), assistantMessage.getMedia());
        memoryManager.addMemory(conversationId, sceneId, assistantMessageWrapper);
        return chatResponse;
    }

    protected ToolResponseMessage executeTool(ChatResponse chatResponse) {
        return toolUtil.executeToolCalls(chatResponse);
    }

    protected ToolResponseMessageWrapper buildToolResponseMessageWrapper(OverAllState state, ToolResponseMessage toolResponseMessage) {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        ToolResponseMessageWrapper toolResponseMessageWrapper = new ToolResponseMessageWrapper(toolResponseMessage);
        memoryManager.addMemory(conversationId, sceneId, toolResponseMessageWrapper);
        return toolResponseMessageWrapper;
    }

    private HashMap<String, Object> getToolInputArguments(ChatResponse chatResponse){
        HashMap<String, Object> toolInputMap = new HashMap<>();
        Optional<Generation> toolCallGeneration = chatResponse.getResults()
                .stream()
                .filter(g -> !org.springframework.util.CollectionUtils.isEmpty(g.getOutput().getToolCalls()))
                .findFirst();

        if (toolCallGeneration.isEmpty()) {
            throw new IllegalStateException("No tool call requested by the chat model");
        }

        AssistantMessage assistantMessage = toolCallGeneration.get().getOutput();
        for (AssistantMessage.ToolCall toolCall : assistantMessage.getToolCalls()) {
            String toolName = toolCall.name();
            String toolInputArguments = toolCall.arguments();
            toolInputMap.put(toolName,toolInputArguments);
        }
        return toolInputMap;
    }

}

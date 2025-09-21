package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.dahuaboke.mpda.core.agent.tools.ToolUtil;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.AssistantMessageWrapper;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<ToolResponseMessage.ToolResponse> responses = toolResponseMessage.getResponses();
        List<Object> extend = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(responses)) {
            responses.forEach(res -> {
                String resData = res.responseData();
                try {
                    ToolResult toolResult = objectMapper.readValue(resData, ToolResult.class);
                    extend.add(toolResult.getData());
                } catch (JsonProcessingException e) {
                    throw new MpdaRuntimeException(e); // TODO
                }
            });
        }
        ToolResponseMessageWrapper toolResponseMessageWrapper = buildToolResponseMessageWrapper(state, toolResponseMessage);


        Map<String,Object> apply = new HashMap<>() {{
            put(Constants.QUERY, toolResponseMessageWrapper);
            put(Constants.IS_TOOL_QUERY, true);
        }};
        if (CollectionUtils.isNotEmpty(extend)) {
            apply.put(Constants.EXTEND, extend);
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
                new AssistantMessageWrapper(conversationId, sceneId, assistantMessage.getText(), assistantMessage.getMetadata(), assistantMessage.getToolCalls()
                        , assistantMessage.getMedia());
        memoryManager.addMemory(conversationId, sceneId, assistantMessageWrapper);
        return chatResponse;
    }

    protected ToolResponseMessage executeTool(ChatResponse chatResponse) {
        return toolUtil.executeToolCalls(chatResponse);
    }

    protected ToolResponseMessageWrapper buildToolResponseMessageWrapper(OverAllState state, ToolResponseMessage toolResponseMessage) {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        ToolResponseMessageWrapper toolResponseMessageWrapper = new ToolResponseMessageWrapper(conversationId, sceneId, toolResponseMessage);
        memoryManager.addMemory(conversationId, sceneId, toolResponseMessageWrapper);
        return toolResponseMessageWrapper;
    }



}

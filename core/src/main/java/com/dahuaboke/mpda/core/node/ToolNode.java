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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ToolNode.class);


    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        ChatResponse chatResponse = chatResponse(state);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();

        ToolResponseMessage toolResponseMessage;
        List<ToolResponseMessage.ToolResponse> responses;
        try {
            toolResponseMessage = executeTool(chatResponse);
            responses = toolResponseMessage.getResponses();
        } catch (Exception e) {
            log.error("toolNode process fail,execute tool is fail",e);
            ToolResult toolResult = ToolResult.error("工具调用失败,工具内部异常,勿再重复调用");
            responses = new ArrayList<>();
            for (AssistantMessage.ToolCall toolCall : toolCalls) {
                responses.add(new ToolResponseMessage.ToolResponse(toolCall.id(), toolCall.name(), objectMapper.writeValueAsString(toolResult)));
            }
            toolResponseMessage = new ToolResponseMessage(responses);
        }

        List<Object> extend = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(responses)) {
            responses.forEach(res -> {
                String resData = res.responseData();
                try {
                    ToolResult toolResult = objectMapper.readValue(resData, ToolResult.class);
                    extend.add(toolResult.getData());
                } catch (JsonProcessingException e) {
                    extend.add(resData);
                }
            });
        }
        ToolResponseMessageWrapper toolResponseMessageWrapper = buildToolResponseMessageWrapper(state, toolResponseMessage);


        Map<String, Object> apply = new HashMap<>() {{
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

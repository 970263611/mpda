package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.streaming.StreamingChatGenerator;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.memory.AssistantMessageWrapper;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * auth: dahua
 * time: 2025/8/22 09:32
 */
@Component
public class HumanNode implements NodeAction {

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        return Map.of(Constants.RESULT, buildResponse(Constants.RESULT, chatResponse.getResult().getOutput().getText(), state));
    }

    private AsyncGenerator<? extends NodeOutput> buildResponse(String key, String content, OverAllState state) {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        ChatResponse chatResponse = new ChatResponse(List.of(new Generation(new AssistantMessageWrapper(conversationId, sceneId, content))));
        Flux<ChatResponse> just = Flux.just(chatResponse);
        return StreamingChatGenerator.builder()
                .startingNode("humanNode")
                .startingState(state)
                .mapResult(
                        response -> Map.of(key, Objects.requireNonNull(response.getResult().getOutput().getText())))
                .build(just);
    }
}

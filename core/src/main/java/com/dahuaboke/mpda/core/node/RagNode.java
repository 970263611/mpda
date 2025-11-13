package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.client.EmbeddingClientManager;
import com.dahuaboke.mpda.core.client.entity.EmbeddingResponse;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/11/13 18:55
 */
@Component
public class RagNode implements NodeAction {

    private EmbeddingClientManager embeddingClientManager;

    public RagNode(EmbeddingClientManager embeddingClientManager) {
        this.embeddingClientManager = embeddingClientManager;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        String text = llmResponse.chatResponse().getResult().getOutput().getText();
        EmbeddingResponse embedding = embeddingClientManager.embed(text);
        return Map.of(Constants.RESULT, embedding);
    }
}

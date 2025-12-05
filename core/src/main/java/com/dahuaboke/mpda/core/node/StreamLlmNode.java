package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.client.ChatClientManager;
import com.dahuaboke.mpda.core.context.consts.Constants;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/8/21 10:16
 */
@Component
public class StreamLlmNode implements NodeAction {

    @Autowired
    private ChatClientManager chatClientManager;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String prompt = state.value(Constants.PROMPT, String.class).get();
        Object query = state.value(Constants.QUERY).get();
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        String key = Constants.RESULT;
        List<String> sceneMerge = state.value(Constants.SCENE_MERGE, List.class).orElse(List.of());
        Boolean isToolQuery = state.value(Constants.IS_TOOL_QUERY, Boolean.class).orElse(false);
        Set<Class<? extends Message>> memoryExclude = state.value(Constants.MEMORY_EXCLUDE, Set.class).get();
        return Map.of(key, chatClientManager.stream(conversationId, sceneId, prompt, query, sceneMerge, isToolQuery, memoryExclude)
                , Constants.IS_TOOL_QUERY, false);
    }
}

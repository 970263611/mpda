package com.dahuaboke.mpda.bot.scenes.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 10:24
 */

public abstract class TranslateNode implements NodeAction {

    private static final Logger log = LoggerFactory.getLogger(TranslateNode.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> apply(OverAllState state) {
        Object query = state.value(Constants.QUERY).get();
        try {
            if (query instanceof ToolResponseMessageWrapper) {
                List<ToolResponseMessage.ToolResponse> responses = ((ToolResponseMessageWrapper) query).getResponses();
                if (!responses.isEmpty()) {
                    ToolResponseMessage.ToolResponse toolResponse = responses.get(0);
                    //翻以前toolResponseData
                    String toolResponseData = toolResponse.responseData();
                    //翻译后toolResponseData
                    String translateToolResponseData = reBuildToolResp(toolResponseData);
                    responses.set(0, new ToolResponseMessage.ToolResponse(toolResponse.id(), toolResponse.name(), translateToolResponseData));
                }
            }
        } catch (Exception e) {
            log.error("TranslateNode Exception {}", e.getMessage());
        }
        Map<String, Object> apply = new HashMap<>() {{
            put(Constants.QUERY, query);
        }};
        return apply;
    }

    public String reBuildToolResp(String toolResponseData) {
        String translateToolResponseData = toolResponseData;
        return translateToolResponseData;
    }
}

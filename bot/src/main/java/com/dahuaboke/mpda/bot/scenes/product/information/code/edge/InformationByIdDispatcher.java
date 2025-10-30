package com.dahuaboke.mpda.bot.scenes.product.information.code.edge;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.dahuaboke.mpda.bot.scenes.ToolCallContentParse;
import com.dahuaboke.mpda.bot.scenes.product.information.code.InformationByIdAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import java.util.Map;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InformationByIdDispatcher implements EdgeAction {

    @Autowired
    private InformationByIdAgentPrompt informationPrompt;

    @Autowired
    private ToolCallContentParse toolCallContentParse;

    @Override
    public String apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        if (chatResponse.hasToolCalls()) {
            informationPrompt.changePrompt("tool");
            state.input(Map.of(Constants.PROMPT, informationPrompt.description()));
            return "go_tool";
        }
        return toolCallContentParse.parseContent(informationPrompt, chatResponse, state);
    }

}

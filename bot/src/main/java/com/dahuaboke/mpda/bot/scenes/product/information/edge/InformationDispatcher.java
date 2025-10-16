package com.dahuaboke.mpda.bot.scenes.product.information.edge;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.dahuaboke.mpda.bot.scenes.product.information.InformationAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InformationDispatcher implements EdgeAction {

    private static final Pattern pattern = Pattern.compile("<tool_call\\s*[^>]*>(.*?)(</tool_call>|$)", Pattern.DOTALL);

    @Autowired
    private InformationAgentPrompt informationPrompt;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        if (chatResponse.hasToolCalls()) {
            informationPrompt.changePrompt("tool");
            state.input(Map.of(Constants.PROMPT, informationPrompt.description()));
            return "go_tool";
        }
        String text = chatResponse.getResult().getOutput().getText();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            AssistantMessage output = chatResponse.getResult().getOutput();
            String toolData = matcher.group(1).trim();
            AssistantMessage.ToolCall toolCall = objectMapper.readValue(toolData, AssistantMessage.ToolCall.class);
            List tools = List.of(new AssistantMessage.ToolCall(UUID.randomUUID().toString(), "function", toolCall.name(), toolCall.arguments()));
            List media = output.getMedia() == null ? List.of() : output.getMedia();
            ChatResponse newChatResponse = new ChatResponse(
                    List.of(new Generation(new AssistantMessage(
                            output.getText(), output.getMetadata(), tools, media))));
            informationPrompt.changePrompt("tool");
            state.input(Map.of(Constants.RESULT, new LlmResponse(newChatResponse), Constants.PROMPT, informationPrompt.description()));
            return "go_tool";
        }
        return "go_human";
    }

}

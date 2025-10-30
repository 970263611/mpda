package com.dahuaboke.mpda.bot.scenes;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolCallContentParse {

    private static final Logger log = LoggerFactory.getLogger(ToolCallContentParse.class);

    private static final Pattern pattern = Pattern.compile("<tool_call\\s*[^>]*>(.*?)(</tool_call>|$)", Pattern.DOTALL);

    @Autowired
    private ObjectMapper objectMapper;

    public String parseContent(AbstractProductAgentPrompt prompt, ChatResponse chatResponse, OverAllState state) throws JsonProcessingException {
        String text = chatResponse.getResult().getOutput().getText();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            AssistantMessage output = chatResponse.getResult().getOutput();
            String toolData = matcher.group(1).trim();
            log.info("ChatResponse content is tool, toolData is {}", toolData);
            if (toolData.contains("]")) {
                toolData = toolData.replaceAll("]", "}");
            }
            List tools;
            List media;
            try {
                JsonNode toolDatajsonNode = objectMapper.readTree(toolData);
                JsonNode arguments = toolDatajsonNode.get("arguments");
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", toolDatajsonNode.get("name").asText());
                if (arguments.isObject()) {
                    String argumentsFinal = objectMapper.writeValueAsString(toolDatajsonNode.get("arguments"));
                    hashMap.put("arguments", argumentsFinal);

                } else {
                    hashMap.put("arguments", arguments);
                }
                String finalToolData = objectMapper.writeValueAsString(hashMap);
                log.info("ChatResponse content is tool, finalToolData is {}", finalToolData);
                AssistantMessage.ToolCall toolCall = objectMapper.readValue(finalToolData, AssistantMessage.ToolCall.class);
                tools = List.of(new AssistantMessage.ToolCall(UUID.randomUUID().toString(), "function", toolCall.name(), toolCall.arguments()));
                media = output.getMedia() == null ? List.of() : output.getMedia();
            } catch (Exception e) {
                log.error("ToolCall json process fail", e);
                ChatResponse newChatResponse = new ChatResponse(
                        List.of(new Generation(new AssistantMessage(
                                "很抱歉,邮小盈暂时还不能回答您的问题，我们正在努力开发中~您可以继续问我: 查询产品信息;产品对比(目前只支持两个产品对比哟);个性化推荐产品;定时市场产品报告;", output.getMetadata()))));
                state.input(Map.of(Constants.RESULT, new LlmResponse(newChatResponse), Constants.PROMPT, prompt.description()));
                return "go_human";
            }
            ChatResponse newChatResponse = new ChatResponse(
                    List.of(new Generation(new AssistantMessage(
                            output.getText(), output.getMetadata(), tools, media))));
            prompt.changePrompt("tool");
            state.input(Map.of(Constants.RESULT, new LlmResponse(newChatResponse), Constants.PROMPT, prompt.description()));
            return "go_tool";
        }
        return "go_human";
    }

}

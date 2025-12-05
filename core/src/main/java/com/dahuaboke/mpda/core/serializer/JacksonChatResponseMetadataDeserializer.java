package com.dahuaboke.mpda.core.serializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.metadata.PromptMetadata;
import org.springframework.ai.openai.metadata.OpenAiRateLimit;

import java.io.IOException;

/**
 * auth: dahua
 * time: 2025/12/3 16:42
 */
public class JacksonChatResponseMetadataDeserializer extends StdDeserializer<ChatResponseMetadata> {

    public JacksonChatResponseMetadataDeserializer() {
        super(ChatResponseMetadata.class);
    }

    @Override
    public ChatResponseMetadata deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = (ObjectMapper) ctxt.getParser().getCodec();
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        String id = treeNode.get("id").toString();
        String model = treeNode.get("model").toString();
        TreeNode rateLimitNode = treeNode.get("rateLimit");
        OpenAiRateLimit rateLimit = objectMapper.treeToValue(rateLimitNode, OpenAiRateLimit.class);
        TreeNode usageNode = treeNode.get("usage");
        DefaultUsage usage = objectMapper.treeToValue(usageNode, DefaultUsage.class);
        return ChatResponseMetadata.builder().id(id).model(model).rateLimit(rateLimit).usage(usage).promptMetadata(PromptMetadata.empty()).build();
    }
}

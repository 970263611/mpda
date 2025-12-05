package com.dahuaboke.mpda.core.serializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;

import java.io.IOException;

/**
 * auth: dahua
 * time: 2025/12/3 16:38
 */
public class JacksonGenerationDeserializer extends StdDeserializer<Generation> {

    protected JacksonGenerationDeserializer() {
        super(Generation.class);
    }

    @Override
    public Generation deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = (ObjectMapper) ctxt.getParser().getCodec();
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        TreeNode assistantMessageNode = treeNode.get("assistantMessage");
        TreeNode chatGenerationMetadataNode = treeNode.get("chatGenerationMetadata");
        AssistantMessage assistantMessage = objectMapper.treeToValue(assistantMessageNode, AssistantMessage.class);
        CustomChatGenerationMetadata customChatGenerationMetadata = objectMapper.treeToValue(chatGenerationMetadataNode, CustomChatGenerationMetadata.class);
        return new Generation(assistantMessage, customChatGenerationMetadata);
    }
}

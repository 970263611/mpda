package com.dahuaboke.mpda.core.serializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * auth: dahua
 * time: 2025/12/3 16:19
 */
public class JacksonChatResponseDeserializer extends StdDeserializer<ChatResponse> {

    protected JacksonChatResponseDeserializer() {
        super(ChatResponse.class);
    }

    @Override
    public ChatResponse deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = (ObjectMapper) ctxt.getParser().getCodec();
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        List<Generation> generations = new ArrayList<>();
        TreeNode generationsNodes = treeNode.get("generations");
        if (generationsNodes.isArray()) {
            ArrayNode arrayNodes = (ArrayNode) generationsNodes;
            arrayNodes.forEach(node -> {
                try {
                    generations.add(objectMapper.treeToValue(node, Generation.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        TreeNode chatResponseMetadataNode = treeNode.get("chatResponseMetadata");
        ChatResponseMetadata chatResponseMetadata = objectMapper.treeToValue(chatResponseMetadataNode, ChatResponseMetadata.class);
        return new ChatResponse(generations, chatResponseMetadata);
    }
}

package com.dahuaboke.mpda.core.serializer;

import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: zhangjie fw
 * @author: ZHANGSHUHAN
 * @date: 2025/12/09
 */
public class JacksonToolResponseMessageWrapperDeserializer extends StdDeserializer<ToolResponseMessageWrapper> {

    protected JacksonToolResponseMessageWrapperDeserializer() {
        super(ToolResponseMessageWrapper.class);
    }

    @Override
    public ToolResponseMessageWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = (ObjectMapper) ctxt.getParser().getCodec();
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        List<ToolResponseMessage.ToolResponse> toolResponses = new ArrayList<>();
        TreeNode responseNodes = treeNode.get("responses");
        if (responseNodes.isArray()) {
            ArrayNode arrayNodes = (ArrayNode) responseNodes;
            arrayNodes.forEach(node -> {
                try {
                    toolResponses.add(objectMapper.treeToValue(node, ToolResponseMessage.ToolResponse.class));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        TreeNode metadataNode = treeNode.get("metadata");
        Map metadata = objectMapper.treeToValue(metadataNode, Map.class);
        String conversationId = (String) metadata.get("conversationId");
        String sceneName = (String) metadata.get("sceneName");
        Long time = (Long) ((List) metadata.get("time")).get(1);
        return new ToolResponseMessageWrapper(conversationId, sceneName, new ToolResponseMessage(toolResponses, metadata), time);
    }
}

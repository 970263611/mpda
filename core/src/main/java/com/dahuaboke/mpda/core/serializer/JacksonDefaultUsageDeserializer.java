package com.dahuaboke.mpda.core.serializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.openai.api.OpenAiApi;

import java.io.IOException;

/**
 * auth: dahua
 * time: 2025/12/3 16:42
 */
public class JacksonDefaultUsageDeserializer extends StdDeserializer<DefaultUsage> {

    public JacksonDefaultUsageDeserializer() {
        super(DefaultUsage.class);
    }

    @Override
    public DefaultUsage deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = (ObjectMapper) ctxt.getParser().getCodec();
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        String promptTokensNode = treeNode.get("promptTokens").toString();
        String completionTokensNode = treeNode.get("completionTokens").toString();
        String totalTokensNode = treeNode.get("totalTokens").toString();
        TreeNode nativeUsageNode = treeNode.get("nativeUsage");
        Integer promptTokens = promptTokensNode == null || "null".equals(promptTokensNode) ? null : Integer.parseInt(promptTokensNode);
        Integer completionTokens = completionTokensNode == null || "null".equals(completionTokensNode) ? null : Integer.parseInt(completionTokensNode);
        int totalTokens = totalTokensNode == null || "null".equals(totalTokensNode) ? 0 : Integer.parseInt(totalTokensNode);
        Object nativeUsage = nativeUsageNode == null || "null".equals(nativeUsageNode) ? null : objectMapper.treeToValue(nativeUsageNode, OpenAiApi.Usage.class);
        return new DefaultUsage(promptTokens, completionTokens, totalTokens, nativeUsage);
    }
}

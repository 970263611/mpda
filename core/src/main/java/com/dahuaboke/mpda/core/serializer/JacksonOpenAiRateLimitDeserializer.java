package com.dahuaboke.mpda.core.serializer;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.ai.openai.metadata.OpenAiRateLimit;

import java.io.IOException;
import java.time.Duration;

/**
 * auth: dahua
 * time: 2025/12/3 16:42
 */
public class JacksonOpenAiRateLimitDeserializer extends StdDeserializer<OpenAiRateLimit> {

    public JacksonOpenAiRateLimitDeserializer() {
        super(OpenAiRateLimit.class);
    }

    @Override
    public OpenAiRateLimit deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JacksonException {
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        String requestsLimitNode = treeNode.get("requestsLimit").toString();
        String requestsRemainingNode = treeNode.get("requestsRemaining").toString();
        String tokensLimitNode = treeNode.get("tokensLimit").toString();
        String tokensRemainingNode = treeNode.get("tokensRemaining").toString();
        String requestsResetNode = treeNode.get("requestsReset").toString();
        String tokensResetNode = treeNode.get("tokensReset").toString();
        Long requestsLimit = requestsLimitNode == null || "null".equals(requestsLimitNode) ? null : Long.parseLong(requestsLimitNode);
        Long requestsRemaining = requestsRemainingNode == null || "null".equals(requestsRemainingNode) ? null : Long.parseLong(requestsRemainingNode);
        Long tokensLimit = tokensLimitNode == null || "null".equals(tokensLimitNode) ? null : Long.parseLong(tokensLimitNode);
        Long tokensRemaining = tokensRemainingNode == null || "null".equals(tokensRemainingNode) ? null : Long.parseLong(tokensRemainingNode);
        Duration requestsReset = null;
        try {
            requestsReset = requestsResetNode == null || "null".equals(requestsResetNode) ? null : Duration.parse(requestsResetNode);
        } catch (Exception e) {
        }
        Duration tokensReset = null;
        try {
            tokensReset = tokensResetNode == null || "null".equals(tokensResetNode) ? null : Duration.parse(tokensResetNode);
        } catch (Exception e) {
        }
        return new OpenAiRateLimit(requestsLimit, requestsRemaining, requestsReset, tokensLimit, tokensRemaining, tokensReset);
    }
}

package com.dahuaboke.mpda.core.serializer;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.serializer.plain_text.jackson.SpringAIJacksonStateSerializer;
import com.alibaba.cloud.ai.graph.state.AgentStateFactory;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.metadata.OpenAiRateLimit;

/**
 * auth: dahua
 * time: 2025/12/3 16:15
 */
public class CustomSpringAIJacksonStateSerializer extends SpringAIJacksonStateSerializer {

    public CustomSpringAIJacksonStateSerializer(AgentStateFactory<OverAllState> stateFactory) {
        super(stateFactory);

        var module = new SimpleModule();
        module.addDeserializer(ChatResponse.class, new JacksonChatResponseDeserializer());
        module.addDeserializer(Generation.class, new JacksonGenerationDeserializer());
        module.addDeserializer(ChatResponseMetadata.class, new JacksonChatResponseMetadataDeserializer());
        module.addDeserializer(OpenAiRateLimit.class, new JacksonOpenAiRateLimitDeserializer());
        module.addDeserializer(DefaultUsage.class, new JacksonDefaultUsageDeserializer());
        module.addDeserializer(ToolResponseMessageWrapper.class, new JacksonToolResponseMessageWrapperDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
    }
}

package com.dahuaboke.mpda.core.config;

import com.dahuaboke.mpda.core.agent.prompt.SystemAgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.strategy.FindSceneStrategy;
import com.dahuaboke.mpda.core.agent.scene.strategy.PlanStrategy;
import com.dahuaboke.mpda.core.agent.scene.strategy.RouteStrategy;
import com.dahuaboke.mpda.core.agent.scene.unknown.DefaultUnknownWrapper;
import com.dahuaboke.mpda.core.agent.scene.unknown.UnknownWrapper;
import com.dahuaboke.mpda.core.client.ChatClientManager;
import com.dahuaboke.mpda.core.client.RerankerClientManager;
import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.rag.config.RagConfiguration;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@Import(RagConfiguration.class)
@ComponentScan(basePackages = "com.dahuaboke.mpda.core")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChatClientManager chatClientManager(ChatModel chatModel, SystemAgentPrompt commonPrompt,
                                               MemoryManager memoryManager, ToolCallbackProvider tools) {
        return new ChatClientManager(chatModel, commonPrompt, memoryManager, tools);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "mpda.rerank.enabled", havingValue = "true")
    public RerankerClientManager rerankerClientManager() {
        return new RerankerClientManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public UnknownWrapper unknownWrapper() {
        return new DefaultUnknownWrapper();
    }

    @Bean
    public FindSceneStrategy findSceneStrategy(@Value("${mpda.scene.strategy:route}") String strategy) {
        switch (strategy) {
            case "plan":
                return new PlanStrategy();
            case "route":
                return new RouteStrategy();
        }
        throw new MpdaIllegalArgumentException("Unknown find scene strategy: " + strategy);
    }

    @Bean
    public ObjectMapper utf8ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.COMBINE_UNICODE_SURROGATES_IN_UTF8, true);
        return objectMapper;
    }

//    @Bean
//    @Primary
//    public WebClient.Builder openAiWebClientBuilder(ObjectMapper objectMapper) {
//        return WebClient.builder().codecs(configurer -> {
//            configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
//            configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
//        });
//    }
}

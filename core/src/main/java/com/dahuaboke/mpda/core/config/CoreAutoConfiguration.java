package com.dahuaboke.mpda.core.config;

import com.dahuaboke.mpda.core.agent.prompt.SystemAgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.strategy.SceneFinderStrategy;
import com.dahuaboke.mpda.core.agent.scene.strategy.PlanFinderStrategy;
import com.dahuaboke.mpda.core.agent.scene.strategy.RouteFinderStrategy;
import com.dahuaboke.mpda.core.agent.scene.unknown.DefaultUnknownWrapper;
import com.dahuaboke.mpda.core.agent.scene.unknown.UnknownWrapper;
import com.dahuaboke.mpda.core.client.ChatClientManager;
import com.dahuaboke.mpda.core.event.EventPublisher;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.monitor.persistence.PersistenceHandler;
import com.dahuaboke.mpda.core.monitor.persistence.PersistenceManager;
import com.dahuaboke.mpda.core.monitor.persistence.PersistenceService;
import com.dahuaboke.mpda.core.rag.config.RagConfiguration;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import static com.dahuaboke.mpda.core.config.MpdaMonitorProperties.PERSISTENCE_ENABLE;
import static com.dahuaboke.mpda.core.config.MpdaMonitorProperties.PERSISTENCE_ENABLE_VALUE;

@AutoConfiguration
@Import(RagConfiguration.class)
@ComponentScan(basePackages = "com.dahuaboke.mpda.core")
@EnableConfigurationProperties({
        MpdaMemoryProperties.class, MpdaMonitorProperties.class, MpdaRerankProperties.class, MpdaSceneProperties.class, MpdaTraceProperties.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChatClientManager chatClientManager(ChatModel chatModel, SystemAgentPrompt commonPrompt,
                                               MemoryManager memoryManager, ToolCallbackProvider tools, EventPublisher eventPublisher) {
        return new ChatClientManager(chatModel, commonPrompt, memoryManager, tools, eventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public UnknownWrapper unknownWrapper() {
        return new DefaultUnknownWrapper();
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

    @Bean
    @ConditionalOnProperty(value = PERSISTENCE_ENABLE, havingValue = PERSISTENCE_ENABLE_VALUE)
    public PersistenceManager persistenceManager(ObjectMapper objectMapper, PersistenceHandler persistenceHandler) {
        return new PersistenceManager(objectMapper, persistenceHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistenceHandler persistenceHandler(MpdaMonitorProperties properties) {
        return new PersistenceService(properties.getPersistence().getMax());
    }
}

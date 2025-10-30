package com.dahuaboke.mpda.core.config;

import com.dahuaboke.mpda.core.agent.prompt.CommonAgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.DefaultUnknownWrapper;
import com.dahuaboke.mpda.core.agent.scene.UnknownWrapper;
import com.dahuaboke.mpda.core.client.ChatClientManager;
import com.dahuaboke.mpda.core.client.RerankerClientManager;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.rag.config.RagConfiguration;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(RagConfiguration.class)
@ComponentScan("com.dahuaboke.mpda.core")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChatClientManager chatClientManager(ChatModel chatModel, CommonAgentPrompt commonPrompt, MemoryManager memoryManager, ToolCallbackProvider tools) {
        return new ChatClientManager(chatModel, commonPrompt, memoryManager, tools);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "rerank.enabled", havingValue = "true")
    public RerankerClientManager rerankerClientManager() {
        return new RerankerClientManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public UnknownWrapper unknownWrapper() {
        return new DefaultUnknownWrapper();
    }
}

package com.dahuaboke.mpda.core.config;

import com.dahuaboke.mpda.core.agent.prompt.CommonAgentPrompt;
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
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
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
}

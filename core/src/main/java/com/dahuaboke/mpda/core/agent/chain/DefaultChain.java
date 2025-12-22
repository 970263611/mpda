package com.dahuaboke.mpda.core.agent.chain;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/8/21 14:11
 */
public class DefaultChain extends AbstractChain {

    private DefaultChain(Graph graph, List<AgentPrompt> agentPrompt, CacheManager cacheManager, Set<Class<? extends Message>> memoryExclude) {
        super(graph, agentPrompt, cacheManager, memoryExclude);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SceneResponse executeGraph() throws MpdaRuntimeException {
        return graph.execute(attribute);
    }

    @Override
    public Flux<SceneResponse> executeGraphAsync() throws MpdaRuntimeException {
        return graph.executeAsync(attribute);
    }

    public static final class Builder {

        private Graph graph;
        private List<AgentPrompt> agentPrompt;
        private CacheManager cacheManager;
        private Set<Class<? extends Message>> memoryExclude;

        private Builder() {
        }

        public Builder graph(Graph graph) {
            this.graph = graph;
            return this;
        }

        public Builder prompt(List<AgentPrompt> agentPrompt) {
            this.agentPrompt = agentPrompt;
            return this;
        }

        public Builder cacheManager(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
            return this;
        }

        public Builder memoryExclude(Set<Class<? extends Message>> memoryExclude) {
            this.memoryExclude = memoryExclude;
            return this;
        }

        public DefaultChain build() {
            return new DefaultChain(graph, agentPrompt, cacheManager, memoryExclude);
        }
    }
}

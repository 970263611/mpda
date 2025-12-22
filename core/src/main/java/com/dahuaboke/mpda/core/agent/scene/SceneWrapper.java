package com.dahuaboke.mpda.core.agent.scene;

import com.dahuaboke.mpda.core.agent.chain.Chain;
import com.dahuaboke.mpda.core.agent.prompt.AgentPromptLoader;
import com.dahuaboke.mpda.core.agent.prompt.entity.AgentPromptEntity;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/08/21 8:50
 */
public class SceneWrapper {

    private final Scene scene;
    private final Chain chain;
    protected Set<SceneWrapper> childrenWrapper;
    private final AgentPromptLoader agentPromptLoader;
    private final CacheManager cacheManager;

    protected SceneWrapper(Chain chain, Scene scene, AgentPromptLoader agentPromptLoader, CacheManager cacheManager) {
        this.chain = chain;
        this.scene = scene;
        this.agentPromptLoader = agentPromptLoader;
        this.cacheManager = cacheManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDescription() {
        return scene == null ? "unknown" : scene.description();
    }

    public String getSceneName() {
        return scene == null ? "unknown" : scene.getClass().getName();
    }

    public void addChildWrapper(SceneWrapper childWrapper) {
        if (childrenWrapper == null) {
            childrenWrapper = new HashSet<>();
        }
        this.childrenWrapper.add(childWrapper);
    }

    public void init() throws MpdaGraphException {
        if (CollectionUtils.isNotEmpty(childrenWrapper) && scene != null && scene.prompt() != null) {
            Map<String, String> collect = childrenWrapper.stream().collect(Collectors.toMap(child -> {
                if (child != null) {
                    return child.getSceneName();
                }
                return "";
            }, child -> {
                if (child != null) {
                    return child.getDescription();
                }
                return "";
            }));
            scene.prompt().stream().forEach(agentPrompt -> {
                agentPrompt.build(collect);
                List<AgentPromptEntity> prompts = agentPromptLoader.extractPrompt(agentPrompt.getClass().getSuperclass().getName());
                prompts.forEach(entity -> {
                    try {
                        cacheManager.setPromptFromFie(entity.getPrompt());
                        agentPrompt.build(collect);
                        String prompt = cacheManager.getPromptFromFie();
                        agentPromptLoader.updatePrompt(entity.getPromptName(), entity.getFindStrategyName(), prompt);
                    } finally {
                        cacheManager.removePromptFromFie();
                    }
                });
            });
        }
        this.chain.init();
    }

    public boolean isEnd() {
        return childrenWrapper == null;
    }

    public Set<SceneWrapper> getChildrenWrapper() {
        return childrenWrapper;
    }

    public SceneResponse apply(CoreContext context) throws MpdaException {
        return chain.slide(context);
    }

    public Flux<SceneResponse> applyAsync(CoreContext context) throws MpdaException {
        return chain.slideAsync(context);
    }

    public static final class Builder {

        private Chain chain;
        private Scene scene;
        private AgentPromptLoader agentPromptLoader;
        private CacheManager cacheManager;

        private Builder() {
        }

        public Builder chain(Chain chain) {
            this.chain = chain;
            return this;
        }

        public Builder scene(Scene scene) {
            this.scene = scene;
            return this;
        }

        public Builder agentPromptLoader(AgentPromptLoader agentPromptLoader) {
            this.agentPromptLoader = agentPromptLoader;
            return this;
        }

        public Builder cacheManager(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
            return this;
        }

        public SceneWrapper build() {
            return new SceneWrapper(chain, scene, agentPromptLoader, cacheManager);
        }
    }
}

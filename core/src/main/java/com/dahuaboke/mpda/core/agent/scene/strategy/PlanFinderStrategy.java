package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.prompt.AgentPromptLoader;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.config.MpdaSceneProperties;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
@Component
public class PlanFinderStrategy extends AbstractSceneFinderStrategy {

    private static final Logger logger = LoggerFactory.getLogger(PlanFinderStrategy.class);

    public PlanFinderStrategy(MpdaSceneProperties properties, CacheManager cacheManager
            , SceneFinderManager sceneFinderManager, AgentPromptLoader agentPromptLoader) {
        super(properties, cacheManager, sceneFinderManager, agentPromptLoader);
    }

    @Override
    public List<SceneWrapper> findScene(CoreContext context) {
        return List.of(cacheManager.getRootWrapper());
    }

    @Override
    public void init() {
        try {
            cacheManager.getRootWrapper().init();
        } catch (MpdaGraphException e) {
            logger.error("Error while initializing scene", e);
        }
    }

    @Override
    public String name() {
        return "plan";
    }
}

package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.unknown.UnknownWrapper;
import com.dahuaboke.mpda.core.config.MpdaSceneProperties;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
public abstract class AbstractSceneFinderStrategy implements SceneFinderStrategy {

    protected CacheManager cacheManager;
    @Autowired
    protected UnknownWrapper unknownWrapper;
    protected int sceneRetry;
    protected volatile boolean isInit = false;

    public AbstractSceneFinderStrategy(MpdaSceneProperties properties, CacheManager cacheManager, SceneFinderManager sceneFinderManager) {
        this.sceneRetry = properties.getRetry();
        this.cacheManager = cacheManager;
        sceneFinderManager.register(this.name(), this);
    }

    @Override
    public List<SceneWrapper> find(CoreContext context) throws MpdaException {
        String sceneName = context.getSceneName();
        if (StringUtils.hasLength(sceneName)) {
            return List.of(cacheManager.getSceneWrapperByName(sceneName));
        }
        return findScene(context);
    }

    public abstract List<SceneWrapper> findScene(CoreContext context) throws MpdaException;
}

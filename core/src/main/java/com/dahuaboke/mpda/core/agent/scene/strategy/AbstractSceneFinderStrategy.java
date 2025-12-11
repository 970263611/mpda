package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.unknown.UnknownWrapper;
import com.dahuaboke.mpda.core.config.MpdaSceneProperties;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
public abstract class AbstractSceneFinderStrategy implements SceneFinderStrategy {

    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    protected SceneFinderManager sceneFinderManager;
    @Autowired
    protected UnknownWrapper unknownWrapper;
    @Autowired
    protected MpdaSceneProperties properties;
    protected int sceneRetry;
    protected volatile boolean isInit = false;

    public AbstractSceneFinderStrategy() {
        this.sceneRetry = properties.getRetry();
        sceneFinderManager.register(this.name(), this);
    }

    @Override
    public List<SceneWrapper> find(CoreContext context) throws MpdaException {

        return findScene(context);
    }

    public abstract List<SceneWrapper> findScene(CoreContext context) throws MpdaException;
}

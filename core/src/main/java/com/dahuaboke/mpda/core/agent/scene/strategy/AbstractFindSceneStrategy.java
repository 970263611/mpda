package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.unknown.UnknownWrapper;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
public abstract class AbstractFindSceneStrategy implements FindSceneStrategy {

    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    protected UnknownWrapper unknownWrapper;
    @Value("${mpda.scene.retry:1}")
    protected int sceneRetry;
    protected volatile boolean isInit = false;

    @Override
    public List<SceneWrapper> find(CoreContext context) throws MpdaException {

        return findScene(context);
    }

    public abstract List<SceneWrapper> findScene(CoreContext context) throws MpdaException;
}

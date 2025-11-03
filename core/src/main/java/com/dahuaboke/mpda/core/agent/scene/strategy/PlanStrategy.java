package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
public class PlanStrategy extends AbstractFindSceneStrategy {

    @Override
    public List<SceneWrapper> findScene(CoreContext context) throws MpdaException {
        if (!isInit) {
            lazyInit();
        }
        return List.of(cacheManager.getRootWrapper());
    }

    private void lazyInit() throws MpdaGraphException {
        cacheManager.getRootWrapper().init();
    }
}

package com.dahuaboke.mpda.core.agent.scene;


import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.agent.scene.strategy.SceneFinderStrategy;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/8/21 14:41
 */
@Component
public class SceneManager {

    @Autowired
    private SceneFinderStrategy sceneFinderStrategy;
    @Autowired
    private CacheManager cacheManager;

    public SceneResponse apply(CoreContext context) throws MpdaException {
        List<SceneWrapper> sceneWrappers = find(context);
        for (SceneWrapper sceneWrapper : sceneWrappers) {
            try {
                context.setSceneId(sceneWrapper.getSceneId());
                cacheManager.setContext(context);
                return sceneWrapper.apply(context);
            } finally {
                cacheManager.removeContext();
            }
        }
        throw new MpdaException("No scene found");
    }

    public Flux<SceneResponse> applyAsync(CoreContext context) throws MpdaException {
        List<SceneWrapper> sceneWrappers = find(context);
        for (SceneWrapper sceneWrapper : sceneWrappers) {
            try {
                context.setSceneId(sceneWrapper.getSceneId());
                cacheManager.setContext(context);
                return sceneWrapper.applyAsync(context);
            } finally {
                cacheManager.removeContext();
            }
        }
        throw new MpdaException("No scene found");
    }

    private List<SceneWrapper> find(CoreContext context) throws MpdaException {
        return sceneFinderStrategy.find(context);
    }
}

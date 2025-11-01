package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.unknown.UnknownWrapper;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
public abstract class AbstractFindSceneStrategy implements FindSceneStrategy {


    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    protected UnknownWrapper unknownWrapper;
    @Value("${mpda.scene.retry:3}")
    protected int sceneRetry;
    private volatile boolean isInit = false;

    @Override
    public List<SceneWrapper> find(CoreContext context) throws MpdaException {
        if (!isInit) {
            lazyInit();
        }
        return findScene(context);
    }

    public abstract List<SceneWrapper> findScene(CoreContext context) throws MpdaException;

    public void lazyInit() {
        Map<String, SceneWrapper> sceneNameWrappers = cacheManager.getSceneNameWrappers();
        cacheManager.getScenes().stream().forEach(scene -> {
            Class<? extends Scene> parent = scene.parent();
            Scene parentScene = SpringUtil.getBean(parent);
            SceneWrapper parentSceneWrapper = sceneNameWrappers.get(parentScene.getClass().getSimpleName());
            SceneWrapper childSceneWrapper = sceneNameWrappers.get(scene.getClass().getSimpleName());
            parentSceneWrapper.addChildWrapper(childSceneWrapper);
        });
        sceneNameWrappers.values().stream().forEach(sceneWrapper -> {
            try {
                cacheManager.addScenedWrapper(sceneWrapper.getSceneId(), sceneWrapper);
                sceneWrapper.init();
            } catch (MpdaGraphException e) {
                e.printStackTrace(); //TODO
            }
        });
        isInit = true;
    }
}

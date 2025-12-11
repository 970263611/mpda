package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
@Component
public class RouteFinderStrategy extends AbstractSceneFinderStrategy {

    @Override
    public List<SceneWrapper> findScene(CoreContext context) throws MpdaException {
        if (!isInit) {
            lazyInit();
        }
        return List.of(next(context));
    }

    private SceneWrapper next(CoreContext context) throws MpdaException {
        SceneWrapper runtimeWrapper = cacheManager.getRootWrapper();
        while (!runtimeWrapper.isEnd()) {
            try {
                context.setSceneId(runtimeWrapper.getSceneId());
                cacheManager.setContext(context);
                runtimeWrapper = next(context, runtimeWrapper);
            } finally {
                cacheManager.removeContext();
            }
        }
        return runtimeWrapper;
    }

    private SceneWrapper next(CoreContext context, SceneWrapper runtimeWrapper) throws MpdaException {
        return next(context, runtimeWrapper, 0);
    }

    private SceneWrapper next(CoreContext context, SceneWrapper runtimeWrapper, int retry) throws MpdaException {
        SceneResponse execute = runtimeWrapper.apply(context);
        String output = execute.output();
        if (output.startsWith("<think>")) {
            output = output.replaceFirst("(?s)<think>.*?</think>", "");
        }
        String finalExecute = output.trim();
        Set<SceneWrapper> childrenWrapper = runtimeWrapper.getChildrenWrapper();
        Optional<SceneWrapper> match = childrenWrapper.stream().filter(child -> child.getSceneId().equals(finalExecute)).findFirst();
        if (match.isPresent()) {
            return match.get();
        }
        retry++;
        if (retry >= sceneRetry) {
            return unknownWrapper;
        }
        return next(context, runtimeWrapper, retry);
    }

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

    @Override
    public String name() {
        return "route";
    }
}

package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.config.MpdaSceneProperties;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(RouteFinderStrategy.class);

    public RouteFinderStrategy(MpdaSceneProperties properties, CacheManager cacheManager, SceneFinderManager sceneFinderManager) {
        super(properties, cacheManager, sceneFinderManager);
    }

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
                context.setSceneName(runtimeWrapper.getSceneName());
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
        Optional<SceneWrapper> match = childrenWrapper.stream().filter(child -> child.getSceneName().equals(finalExecute)).findFirst();
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
        Map<String, SceneWrapper> sceneWrappers = cacheManager.getSceneWrappers();
        cacheManager.getScenes().stream().forEach(scene -> {
            Class<? extends Scene> parent = scene.parent();
            Scene parentScene = SpringUtil.getBean(parent);
            SceneWrapper parentSceneWrapper = sceneWrappers.get(parentScene.getClass().getName());
            SceneWrapper childSceneWrapper = sceneWrappers.get(scene.getClass().getName());
            parentSceneWrapper.addChildWrapper(childSceneWrapper);
        });
        sceneWrappers.values().stream().forEach(sceneWrapper -> {
            try {
                sceneWrapper.init();
            } catch (MpdaGraphException e) {
                logger.error("Scene init exception {}", sceneWrapper.getSceneName(), e);
            }
        });
        isInit = true;
    }

    @Override
    public String name() {
        return "route";
    }
}

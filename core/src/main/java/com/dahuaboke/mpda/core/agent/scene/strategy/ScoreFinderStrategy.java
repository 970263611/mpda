package com.dahuaboke.mpda.core.agent.scene.strategy;


import com.dahuaboke.mpda.core.agent.prompt.AgentPromptLoader;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.config.MpdaSceneProperties;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/12/12 17:14
 */
@Component
public class ScoreFinderStrategy extends RouteFinderStrategy {

    public ScoreFinderStrategy(MpdaSceneProperties properties, CacheManager cacheManager, SceneFinderManager sceneFinderManager, AgentPromptLoader agentPromptLoader) {
        super(properties, cacheManager, sceneFinderManager, agentPromptLoader);
    }

    @Override
    public List<SceneWrapper> findScene(CoreContext context) throws MpdaException {
        return super.findScene(context);
    }

    @Override
    protected SceneWrapper next(CoreContext context, SceneWrapper runtimeWrapper, int retry) throws MpdaException {
        SceneResponse execute = runtimeWrapper.apply(context);
        String output = execute.output();
        if (output.startsWith("<think>")) {
            output = output.replaceFirst("(?s)<think>.*?</think>", "");
        }
        String finalExecute = output.trim();
        Map<Integer, String> scoreMap = Arrays.stream(finalExecute.split(",")).map(str ->
                str.split(":")
        ).collect(Collectors.toMap(
                arr -> Integer.parseInt(arr[0].trim()),
                arr -> arr[1].trim(),
                (oldValue, newValue) -> newValue,
                HashMap::new
        ));
        Integer maxScore = scoreMap.keySet().stream()
                .max(Integer::compare)
                .orElse(0);
        String sceneName = scoreMap.get(maxScore);
        Set<SceneWrapper> childrenWrapper = runtimeWrapper.getChildrenWrapper();
        Optional<SceneWrapper> match = childrenWrapper.stream().filter(child -> child.getSceneName().equals(sceneName)).findFirst();
        if (match.isPresent()) {
            return match.get();
        }
        retry++;
        if (retry >= sceneRetry) {
            return unknownWrapper;
        }
        return next(context, runtimeWrapper, retry);
    }

    @Override
    public String name() {
        return "score";
    }
}

package com.dahuaboke.mpda.core.agent.scene.strategy;


import com.dahuaboke.mpda.core.exception.MpdaInvocationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/12/11 17:59
 */
@Component
public class SceneFinderManager {

    private Map<String, SceneFinderStrategy> strategyMap = new HashMap<>();

    public void register(String name, SceneFinderStrategy strategy) {
        strategyMap.put(name, strategy);
    }

    public SceneFinderStrategy getStrategy(String name) {
        SceneFinderStrategy sceneFinderStrategy = strategyMap.get(name);
        if (sceneFinderStrategy == null) {
            throw new MpdaInvocationException(name);
        }
        return sceneFinderStrategy;
    }

    public Set<String> strategyNames() {
        return strategyMap.keySet();
    }
}

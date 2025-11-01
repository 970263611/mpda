package com.dahuaboke.mpda.core.agent.scene.strategy;

import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/11/1 19:50
 */
public interface FindSceneStrategy {

    List<SceneWrapper> find(CoreContext context) throws MpdaException;
}

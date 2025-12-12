package com.dahuaboke.mpda.core.agent.scene.unknown;

import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import reactor.core.publisher.Flux;

/**
 * @author: ZHANGSHUHAN
 * @date: 2025/10/28
 */
public abstract class UnknownWrapper extends SceneWrapper {
    protected UnknownWrapper() {
        super(null, null, null, null);
    }

    abstract public String reply();

    @Override
    public Flux<SceneResponse> applyAsync(CoreContext context) throws MpdaException {
        return Flux.just(new SceneResponse(reply(), null));
    }
}

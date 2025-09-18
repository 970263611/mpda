package com.dahuaboke.mpda.core.agent.chain;


import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 08:55
 */
public interface Chain {

    void init() throws MpdaGraphException;

    SceneResponse slide(CoreContext context) throws MpdaException;

    Flux<SceneResponse> slideAsync(CoreContext context) throws MpdaException;
}

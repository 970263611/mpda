package com.dahuaboke.mpda.requirement.web.service;

import com.dahuaboke.mpda.core.agent.scene.SceneManager;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 14:34
 */
@Service
public class ChatService {

    @Autowired
    private SceneManager sceneManager;


    public SceneResponse split(CoreContext context) throws MpdaException {
        return sceneManager.apply(context);
    }
}

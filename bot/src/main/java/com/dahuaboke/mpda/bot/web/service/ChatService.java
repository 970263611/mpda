package com.dahuaboke.mpda.bot.web.service;

import com.dahuaboke.mpda.bot.model.common.CommonResponse;
import com.dahuaboke.mpda.bot.model.common.ResponseCode;
import com.dahuaboke.mpda.bot.model.response.ChatBotResponse;
import com.dahuaboke.mpda.bot.scenes.entity.PlatformExtend;
import com.dahuaboke.mpda.bot.web.WebResponse;
import com.dahuaboke.mpda.core.agent.scene.SceneManager;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 14:34
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private SceneManager sceneManager;


    public Flux<WebResponse> chatStream(CoreContext context) throws MpdaException {

        if (context == null) {
            return Flux.just(new WebResponse(ResponseCode.MISSING_PARAM.getCode(), ResponseCode.MISSING_PARAM.getMsg(), "", null));
        }

        String conversationId = context.getConversationId();
        String query = context.getQuery();
        //String custRiskLvl = context.getCustRiskLvl();

        if (StringUtils.isEmpty(conversationId)) {
            return Flux.just(new WebResponse(ResponseCode.INVALID_SESSION.getCode(), ResponseCode.INVALID_SESSION.getMsg(), "", null));
        }

        if (StringUtils.isEmpty(query)) {
            return Flux.just(new WebResponse(ResponseCode.MISSING_PARAM.getCode(), ResponseCode.MISSING_PARAM.getMsg(), "", null));
        }
        Flux<SceneResponse> sceneResponseFlux = sceneManager.applyAsync(context);
        return sceneResponseFlux.map(sceneResponse -> {
            PlatformExtend platformExtend;
            if (sceneResponse.extend() == null || sceneResponse.extend().graphExtend() == null) {
                platformExtend = new PlatformExtend();
            } else {
                platformExtend = (PlatformExtend) sceneResponse.extend().graphExtend();
            }
            return new WebResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), sceneResponse.output(), platformExtend);
        });
    }

}

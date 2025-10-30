package com.dahuaboke.mpda.bot.scenes.product.information;


import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.dahuaboke.mpda.bot.scenes.product.information.code.InformationByIdScene;
import com.dahuaboke.mpda.bot.scenes.product.information.name.InformationByNameScene;
import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionGraph;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.MemoryMerge;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/10/28 10:14
 */
@Component
public class InformationGraph extends ResolutionGraph {

    @Override
    @MemoryMerge({InformationByIdScene.class, InformationByNameScene.class})
    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return response(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    @MemoryMerge({InformationByIdScene.class, InformationByNameScene.class})
    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return streamResponse(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }
}

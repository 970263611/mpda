package com.dahuaboke.mpda.core.agent.scene;


import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/9/12 15:36
 */
@Component
public class UnknowWrapper extends SceneWrapper {

    private static final String reply = """
            您的问题我还不懂，等我变聪明些就可以回答了。
            """;

    public UnknowWrapper() {
        super(null, null);
    }

    @Override
    public Flux<SceneResponse> applyAsync(CoreContext context) throws MpdaException {
        return Flux.just(new SceneResponse(reply, null));
    }
}

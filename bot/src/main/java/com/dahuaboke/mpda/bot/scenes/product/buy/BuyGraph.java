package com.dahuaboke.mpda.bot.scenes.product.buy;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 16:41
 */
@Component
public class BuyGraph extends AbstractGraph {
    @Override
    public StateGraph buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        return null;
    }

    @Override
    public String execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        return "";
    }

    @Override
    public Flux<String> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        return null;
    }
}

package com.dahuaboke.mpda.bot.scenes.resolution;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.bot.scenes.product.comparison.ComparisonScene;
import com.dahuaboke.mpda.bot.scenes.product.information.code.InformationByIdScene;
import com.dahuaboke.mpda.bot.scenes.product.information.name.InformationByNameScene;
import com.dahuaboke.mpda.bot.scenes.product.marketRanking.MarketRankingScene;
import com.dahuaboke.mpda.bot.scenes.product.recommendation.RecommendationScene;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.MemoryMerge;
import com.dahuaboke.mpda.core.node.LlmNode;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/8/21 09:12
 */
@Component
public class ResolutionGraph extends AbstractGraph {

    @Autowired
    private LlmNode llmNode;

    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))

                    .addEdge(StateGraph.START, "llm")
                    .addEdge("llm", StateGraph.END);
            return Map.of("default", stateGraph);
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    @MemoryMerge({MarketRankingScene.class, ComparisonScene.class, InformationByIdScene.class, InformationByNameScene.class, RecommendationScene.class})
    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return response(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    @MemoryMerge({MarketRankingScene.class, ComparisonScene.class, InformationByIdScene.class, InformationByNameScene.class, RecommendationScene.class})
    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return streamResponse(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public void addMemory(Message message) {
    }

    @Override
    public void addMemory(String conversationId, String sceneId, Message message) {
    }

}

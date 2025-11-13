package com.dahuaboke.mpda.bot.scenes.product.information.name;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.bot.scenes.product.information.code.edge.InformationByIdDispatcher;
import com.dahuaboke.mpda.bot.scenes.product.marketRanking.MarketRankingScene;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.MemoryMerge;
import com.dahuaboke.mpda.core.node.HumanNode;
import com.dahuaboke.mpda.core.node.LlmNode;
import com.dahuaboke.mpda.core.node.StreamLlmNode;
import com.dahuaboke.mpda.core.node.ToolNode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;


import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/8/22 14:09
 */
@Component
public class InformationByNameGraph extends AbstractGraph {

    @Autowired
    private LlmNode llmNode;

    @Autowired
    private StreamLlmNode streamLlmNode;

    @Autowired
    private HumanNode humanNode;

    @Autowired
    private ToolNode toolNode;

    @Autowired
    private InformationByIdDispatcher informationByIdDispatcher;

    @Autowired
    private InformationByNameAgentPrompt informationPrompt;

    @Override
    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))
                    .addNode("streamLlm", node_async(streamLlmNode))
                    .addNode("human", node_async(humanNode))
                    .addNode("tool", node_async(toolNode))

                    .addEdge(StateGraph.START, "llm")
                    .addConditionalEdges("llm", edge_async(informationByIdDispatcher),
                            Map.of("go_human", "human", "go_tool", "tool"))
                    .addEdge("tool", "streamLlm")
                    .addEdge("human", StateGraph.END)
                    .addEdge("streamLlm", StateGraph.END);
            return Map.of("default", stateGraph);
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    @MemoryMerge(MarketRankingScene.class)
    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        informationPrompt.changePrompt("guide");
        attribute.put(Constants.TOOLS, List.of("informationByIdTool"));
        attribute.put(Constants.PROMPT, informationPrompt.description());
        try {
            return response(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    @MemoryMerge(MarketRankingScene.class)
    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        System.out.println("进来查询产品名称场景。。。。");
        informationPrompt.changePrompt("guide");
        attribute.put(Constants.TOOLS, List.of("informationByIdTool"));
        attribute.put(Constants.PROMPT, informationPrompt.description());
        try {
            return streamResponse(attribute, "default");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }
}

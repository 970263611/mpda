package com.dahuaboke.mpda.bot.scenes.product.marketRanking;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.bot.scenes.entity.PlatformExtend;
import com.dahuaboke.mpda.bot.scenes.product.marketRanking.edge.MarketRankingDispatcher;
import com.dahuaboke.mpda.bot.scenes.product.recommendation.RecommendationGraph;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneExtend;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.node.HumanNode;
import com.dahuaboke.mpda.core.node.LlmNode;
import com.dahuaboke.mpda.core.node.StreamLlmNode;
import com.dahuaboke.mpda.core.node.ToolNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingGraph extends AbstractGraph {
    private static final Logger log = LoggerFactory.getLogger(MarketRankingGraph.class);
    @Autowired
    private LlmNode llmNode;

    @Autowired
    private StreamLlmNode streamLlmNode;

    @Autowired
    private HumanNode humanNode;

    @Autowired
    private ToolNode toolNode;

    @Autowired
    private MarketRankingDispatcher marketRankingDispatcher;

    @Autowired
    private MarketRankingAgentPrompt marketRankingPrompt;

    @Override
    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))
                    .addNode("streamLlm", node_async(streamLlmNode))
                    .addNode("human", node_async(humanNode))
                    .addNode("tool", node_async(toolNode))

                    .addEdge(StateGraph.START, "llm")
                    .addConditionalEdges("llm", edge_async(marketRankingDispatcher),
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
    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        attribute.put(Constants.TOOLS, List.of("marketRankingTool"));
        marketRankingPrompt.changePrompt("guide");
        try {
            return response(attribute, "default", attribute.get(Constants.SCENE_ID));
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        attribute.put(Constants.TOOLS, List.of("marketRankingTool"));
        marketRankingPrompt.changePrompt("guide");
        try {
            return streamResponse(attribute, "default", null);
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    /**
     * @param graphExtend 包装额外扩展信息，比如下载链接标识
     * @param toolExtend  包含该场景执行工具函数的，函数名称，参数(通过函数名称判断参数是否是基金代码)
     * @return SceneExtend
     */
    @Override
    public SceneExtend buildSceneExtend(Object graphExtend, Object toolExtend) {
        //市场排名报告场景,通过工具扩展信息，做额外计算，并添加额外图扩展信息
        PlatformExtend platformExtend = new PlatformExtend();
        try {
            if (toolExtend != null && toolExtend instanceof List) {
                platformExtend.setDownloadLink(true);
                List<List<Map<String, Object>>> extend = (List<List<Map<String, Object>>>) toolExtend;
                Map<String, Object> marketRankDto = extend.get(0).get(0);
                platformExtend.setFinBondType((String) marketRankDto.get("finBondType"));
                platformExtend.setPeriod((String) marketRankDto.get("period"));
            }
        } catch (Exception e) {
            log.error("toolExtend---" + toolExtend);
            return new SceneExtend(platformExtend, toolExtend);
        }
        return new SceneExtend(platformExtend, toolExtend);
    }

}

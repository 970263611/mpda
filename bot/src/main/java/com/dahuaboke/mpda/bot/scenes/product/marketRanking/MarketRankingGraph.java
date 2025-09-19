package com.dahuaboke.mpda.bot.scenes.product.marketRanking;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.bot.scenes.product.marketRanking.edge.MarketRankingDispatcher;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingGraph extends AbstractGraph {

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
     * @param graphExtend 包装额外扩展信息，比如下载链接标识，购买链接标识等
     * @param toolExtend  包含该场景执行工具函数的，函数名称，参数(通过函数名称判断参数是否是基金代码)
     * @return SceneExtend
     */
    @Override
    public SceneExtend buildSceneExtend(Object graphExtend, Object toolExtend) {
        //市场排名报告场景,通过工具扩展信息，做额外计算，并添加额外图扩展信息
        Map<String, Object> graphExtendMap = new HashMap<>();
        //TODO 目前市场排名代码为测试数据，后续代码完善再调整此处解析
        if (toolExtend != null) {
            graphExtendMap.put("downloadLink", true);
        }
        return new SceneExtend(graphExtendMap, toolExtend);
    }

}

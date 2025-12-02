package com.dahuaboke.mpda.bot.scenes.product.information.name;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.bot.scenes.product.information.RagNode;
import com.dahuaboke.mpda.bot.scenes.product.information.code.edge.InformationByIdDispatcher;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.node.LlmNode;
import com.dahuaboke.mpda.core.node.StreamLlmNode;
import com.dahuaboke.mpda.core.node.ToolNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
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
    private ToolNode toolNode;

    @Autowired
    private InformationByIdDispatcher informationByIdDispatcher;

    @Autowired
    private InformationByNameAgentPrompt informationPrompt;

    @Autowired
    private RagNode ragNode;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph beforeGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))
                    .addNode("rag", node_async(ragNode))

                    .addEdge(StateGraph.START, "llm")
                    .addEdge("llm", "rag")
                    .addEdge("rag", StateGraph.END);

            StateGraph afterGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))
                    .addNode("streamLlm", node_async(streamLlmNode))
                    .addNode("tool", node_async(toolNode))

                    .addEdge(StateGraph.START, "llm")
                    .addConditionalEdges("llm", edge_async(informationByIdDispatcher),
                            Map.of("go_human", "streamLlm", "go_tool", "tool"))
                    .addEdge("tool", "streamLlm")
                    .addEdge("streamLlm", StateGraph.END);
            return Map.of("before", beforeGraph, "after", afterGraph);
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            attribute.put(Constants.PROMPT, informationPrompt.changePrompt("before"));
            String output = response(attribute, "before").output();
            informationPrompt.buildGuide(output);
            attribute.put(Constants.TOOLS, List.of("informationByIdTool"));
            attribute.put(Constants.PROMPT, informationPrompt.description());
            return response(attribute, "after");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            attribute.put(Constants.PROMPT, informationPrompt.changePrompt("before"));
            String output = response(attribute, "before").output();
            informationPrompt.buildGuide(output);
            attribute.put(Constants.TOOLS, List.of("informationByIdTool"));
            attribute.put(Constants.PROMPT, informationPrompt.description());
            return streamResponse(attribute, "after");
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }
}

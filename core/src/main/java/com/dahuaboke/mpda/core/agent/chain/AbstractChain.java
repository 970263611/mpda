package com.dahuaboke.mpda.core.agent.chain;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.memory.AssistantMessageWrapper;
import com.dahuaboke.mpda.core.memory.UserMessageWrapper;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 09:14
 */
public abstract class AbstractChain implements Chain {

    protected final Graph graph;
    protected final AgentPrompt agentPrompt;
    protected final CacheManager cacheManager;
    protected Map<String, Object> attribute;

    public AbstractChain(Graph graph, AgentPrompt agentPrompt, CacheManager cacheManager) {
        this.graph = graph;
        this.agentPrompt = agentPrompt;
        this.cacheManager = cacheManager;
        attribute = new HashMap<>() {{
            put(Constants.PROMPT, null);
            put(Constants.QUERY, null);
            put(Constants.RESULT, null);
            put(Constants.TOOLS, null);
            put(Constants.IS_TOOL_QUERY, null);
            put(Constants.EXTEND, null);
        }};
    }

    @Override
    public void init() throws MpdaGraphException {
        graph.init(attribute.keySet());
    }

    @Override
    public SceneResponse slide(CoreContext context) throws MpdaRuntimeException {
        prepare(context);
        graph.addMemory(UserMessageWrapper.builder().text(context.getQuery()).build());
        SceneResponse reply = executeGraph();
        graph.addMemory(new AssistantMessageWrapper(reply.output()));
        return reply;
    }

    @Override
    public Flux<SceneResponse> slideAsync(CoreContext context) throws MpdaRuntimeException {
        String conversationId = context.getConversationId();
        String sceneId = context.getSceneId();
        prepare(context);
        graph.addMemory(UserMessageWrapper.builder().text(context.getQuery()).build());
        Flux<SceneResponse> reply = executeGraphAsync();
        StringBuilder replyMessage = new StringBuilder();
        reply.subscribe(replyTemp -> replyMessage.append(replyTemp.output())
                , error -> {
                    // TODO
                }
                , () -> graph.addMemory(conversationId, sceneId, new AssistantMessageWrapper(replyMessage.toString())));
        return reply;
    }

    abstract public SceneResponse executeGraph() throws MpdaRuntimeException;

    abstract public Flux<SceneResponse> executeGraphAsync() throws MpdaRuntimeException;

    private void prepare(CoreContext context) {
        attribute.remove(Constants.TOOLS);
        attribute.put(Constants.PROMPT, agentPrompt.description());
        attribute.put(Constants.QUERY, context.getQuery());
        attribute.put(Constants.CONVERSATION_ID, context.getConversationId());
        attribute.put(Constants.SCENE_ID, context.getSceneId());
        attribute.put(Constants.EXTEND, context.getExtend());
        cacheManager.setAttribute(attribute);
    }
}

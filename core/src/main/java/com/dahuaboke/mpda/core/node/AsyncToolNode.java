package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * auth: dahua
 * time: 2025/8/22 10:24
 */
@Component
public class AsyncToolNode extends ToolNode implements SmartLifecycle { // TODO

    private BlockingQueue<ChatResponse> queue = new LinkedBlockingQueue();
    private Thread pullThread;
    private volatile boolean isRunning = false;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        ChatResponse chatResponse = chatResponse(state);
        queue.offer(chatResponse);
        List<AssistantMessage.ToolCall> toolCalls = chatResponse.getResult().getOutput().getToolCalls();
        List<ToolResponseMessage.ToolResponse> responses = new ArrayList<>();
        toolCalls.forEach(tc -> {
            responses.add(new ToolResponseMessage.ToolResponse(tc.id(), tc.name(), "execute success"));
        });
        ToolResponseMessage toolResponseMessage = new ToolResponseMessage(responses);
        ToolResponseMessageWrapper toolResponseMessageWrapper = buildToolResponseMessageWrapper(state, toolResponseMessage);
        return Map.of(Constants.QUERY, toolResponseMessageWrapper, Constants.IS_TOOL_QUERY, true);
    }

    @Override
    public void start() {
        if (!isRunning) {
            pullThread = new Thread(() -> {
                while (true) {
                    try {
                        ChatResponse chatResponse = queue.take();
                        executeTool(chatResponse);  // TODO 单线程循环执行，可以并发
                    } catch (InterruptedException e) {
//                        throw new MpdaRuntimeException(e);
                    }
                }
            });
            pullThread.start();
            isRunning = true;
        }
    }

    @Override
    public void stop() {
        pullThread.interrupt();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}

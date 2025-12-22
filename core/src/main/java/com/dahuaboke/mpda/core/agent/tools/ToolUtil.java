package com.dahuaboke.mpda.core.agent.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/8/21 11:32
 */
@Component
public class ToolUtil {

    private static final Logger log = LoggerFactory.getLogger(ToolUtil.class);


    @Autowired
    private ToolCallingManager toolCallingManager;

    public ToolResponseMessage executeToolCalls(ChatResponse chatResponse) {
        ToolExecutionResult toolExecutionResult;

        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        log.debug("origin classLoader：{}", originClassLoader);

        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            log.debug("current classLoader：{}", this.getClass().getClassLoader());
            toolExecutionResult = toolCallingManager.executeToolCalls(new Prompt(), chatResponse);
        } finally {
            Thread.currentThread().setContextClassLoader(originClassLoader);
        }

        List<Message> conversationHistory = toolExecutionResult.conversationHistory();
        try {
            return (ToolResponseMessage) conversationHistory.get(conversationHistory.size() - 1);
        } catch (Exception e) {
            return null;
        }
    }
}

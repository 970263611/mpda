package com.dahuaboke.mpda.core.aop;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.trace.TraceManager;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dahuaboke.mpda.core.trace.TraceMessage.TraceType.*;

/**
 * auth: dahua
 * time: 2025/9/17 10:14
 */
@Aspect
@Component
public class NodeAspect {

    private static final Logger logger = LoggerFactory.getLogger(NodeAspect.class);

    @Autowired
    private TraceManager traceManager;

    @Pointcut("within(com.dahuaboke.mpda.core.node..*) && " +
            "within(com.alibaba.cloud.ai.graph.action.NodeAction+) && " +
            "execution(* apply(..))")
    public void nodePointcut() {
    }

    @Before("nodePointcut()")
    public void beforeExecute(JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        OverAllState state = (OverAllState) arg;
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).orElse("unknow");
        Object target = joinPoint.getTarget();
        NodeAction nodeAction = (NodeAction) target;
        TraceMessage traceMessage = new TraceMessage(conversationId, nodeAction.getClass().getSimpleName(), null, null, IN);
        String trace = String.format("%s >>> in >>> %s @time: %d",
                conversationId, nodeAction.getClass().getSimpleName(), System.currentTimeMillis());
        traceManager.addTrace(traceMessage);
        logger.debug(trace);
    }

    @AfterReturning("nodePointcut()")
    public void afterReturnExecute(JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        OverAllState state = (OverAllState) arg;
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).orElse("unknow");
        Object target = joinPoint.getTarget();
        NodeAction nodeAction = (NodeAction) target;
        TraceMessage traceMessage = new TraceMessage(conversationId, nodeAction.getClass().getSimpleName(), null, null, OUT);
        String trace = String.format("%s <<< out <<< %s @time: %d",
                conversationId, nodeAction.getClass().getSimpleName(), System.currentTimeMillis());
        traceManager.addTrace(traceMessage);
        logger.debug(trace);
    }

    @AfterThrowing("nodePointcut()")
    public void afterThrowingExecute(JoinPoint joinPoint, Throwable throwable) {
        Object arg = joinPoint.getArgs()[0];
        OverAllState state = (OverAllState) arg;
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).orElse("unknow");
        Object target = joinPoint.getTarget();
        NodeAction nodeAction = (NodeAction) target;
        TraceMessage traceMessage = new TraceMessage(conversationId, nodeAction.getClass().getSimpleName(), null, null, EXCEPTION);
        String trace = String.format("%s !!! throw exception !!! %s @time: %d",
                conversationId, nodeAction.getClass().getSimpleName(), System.currentTimeMillis(), throwable);
        traceManager.addTrace(traceMessage);
        logger.debug(trace);
    }
}

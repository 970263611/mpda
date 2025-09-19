package com.dahuaboke.mpda.core.aop;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.context.consts.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/17 10:14
 */
@Aspect
@Component
public class NodeAspect {

    private static final Logger logger = LoggerFactory.getLogger(NodeAspect.class);

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
        logger.info("{} >>> in >>> {}", conversationId, nodeAction.getClass().getSimpleName());
    }

    @AfterReturning("nodePointcut()")
    public void afterReturnExecute(JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        OverAllState state = (OverAllState) arg;
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).orElse("unknow");
        Object target = joinPoint.getTarget();
        NodeAction nodeAction = (NodeAction) target;
        logger.info("{} <<< out <<< {}", conversationId, nodeAction.getClass().getSimpleName());
    }

    @AfterThrowing("nodePointcut()")
    public void afterThrowingExecute(JoinPoint joinPoint, Throwable throwable) {
        Object arg = joinPoint.getArgs()[0];
        OverAllState state = (OverAllState) arg;
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).orElse("unknow");
        Object target = joinPoint.getTarget();
        NodeAction nodeAction = (NodeAction) target;
        logger.info("{} !!! throw exception !!! {}", conversationId, nodeAction.getClass().getSimpleName(), throwable);
    }
}

package com.dahuaboke.mpda.core.agent.scene;


import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.trace.TraceManager;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dahuaboke.mpda.core.trace.TraceMessage.RequestType.*;
import static com.dahuaboke.mpda.core.trace.TraceMessage.TraceType.*;

/**
 * auth: dahua
 * time: 2025/9/17 09:18
 */
@Aspect
@Component
public class SceneAspect {

    private static final Logger logger = LoggerFactory.getLogger(SceneAspect.class);

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private TraceManager traceManager;

    @Pointcut("execution(* com.dahuaboke.mpda.core.agent.scene.SceneWrapper+.apply(..)) || " +
            "execution(* com.dahuaboke.mpda.core.agent.scene.SceneWrapper+.applyAsync(..))")
    public void sceneWrapperPointcut() {
    }

    @Before("sceneWrapperPointcut()")
    public void beforeExecute(JoinPoint joinPoint) {
        TraceMessage traceMessage = buildTraceMessage(joinPoint, IN);
        String trace = String.format("%s >>> (%s) in >>> %s(%s) @time: %d",
                traceMessage.getConversationId(), traceMessage.getRequestType(), traceMessage.getSceneName(), traceMessage.getDescription(), System.currentTimeMillis());
        traceManager.addTrace(traceMessage);
        logger.debug(trace);
    }

    @AfterReturning("sceneWrapperPointcut()")
    public void afterReturnExecute(JoinPoint joinPoint) {
        TraceMessage traceMessage = buildTraceMessage(joinPoint, OUT);
        String trace = String.format("%s <<< (%s) out <<< %s(%s) @time: %d",
                traceMessage.getConversationId(), traceMessage.getRequestType(), traceMessage.getSceneName(), traceMessage.getDescription(), System.currentTimeMillis());
        traceManager.addTrace(traceMessage);
        logger.debug(trace);
    }

    @AfterThrowing("sceneWrapperPointcut()")
    public void afterThrowingExecute(JoinPoint joinPoint, Throwable throwable) {
        TraceMessage traceMessage = buildTraceMessage(joinPoint, EXCEPTION);
        String trace = String.format("%s !!! (%s) throw exception !!! %s(%s) : %s @time: %d",
                traceMessage.getConversationId(), traceMessage.getRequestType(), traceMessage.getSceneName(), traceMessage.getDescription(), throwable.getMessage(), System.currentTimeMillis());
        traceManager.addTrace(traceMessage);
        logger.debug(trace, throwable);
    }

    private TraceMessage buildTraceMessage(JoinPoint joinPoint, TraceMessage.TraceType traceType) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        TraceMessage.RequestType requestType;
        if ("apply".equals(methodName)) {
            requestType = SYNC;
        } else if ("applyAsync".equals(methodName)) {
            requestType = ASYNC;
        } else {
            requestType = UNKNOW;
        }
        Object target = joinPoint.getTarget();
        SceneWrapper sceneWrapper = (SceneWrapper) target;
        String conversationId = cacheManager.getContext().getConversationId();
        String sceneName = sceneWrapper.getSceneName();
        String description = sceneWrapper.getDescription();
        return new TraceMessage(conversationId, sceneName, description, requestType, traceType);
    }
}

package com.dahuaboke.mpda.core.aop;


import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.trace.TraceManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        Inner inner = buildInner(joinPoint);
        String trace = String.format("%s >>> (%s) in >>> %s(%s) @time: %d",
                inner.conversationId, inner.type, inner.simpleName, inner.description, System.currentTimeMillis());
        traceManager.addTrace(inner.conversationId, trace);
        logger.debug(trace);
    }

    @AfterReturning("sceneWrapperPointcut()")
    public void afterReturnExecute(JoinPoint joinPoint) {
        Inner inner = buildInner(joinPoint);
        String trace = String.format("%s <<< (%s) out <<< %s(%s) @time: %d",
                inner.conversationId, inner.type, inner.simpleName, inner.description, System.currentTimeMillis());
        traceManager.addTrace(inner.conversationId, trace);
        logger.debug(trace);
    }

    @AfterThrowing("sceneWrapperPointcut()")
    public void afterThrowingExecute(JoinPoint joinPoint, Throwable throwable) {
        Inner inner = buildInner(joinPoint);
        String trace = String.format("%s !!! (%s) throw exception !!! %s(%s) : %s @time: %d",
                inner.conversationId, inner.type, inner.simpleName, inner.description, throwable.getMessage(), System.currentTimeMillis());
        traceManager.addTrace(inner.conversationId, trace);
        logger.debug(trace, throwable);
    }

    private Inner buildInner(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String type;
        if ("apply".equals(methodName)) {
            type = "sync";
        } else if ("applyAsync".equals(methodName)) {
            type = "async";
        } else {
            type = "unknow";
        }
        Object target = joinPoint.getTarget();
        SceneWrapper sceneWrapper = (SceneWrapper) target;
        String conversationId = cacheManager.getContext().getConversationId();
        Class<? extends Scene> sceneClass = sceneWrapper.getSceneClass();
        String simpleName = sceneClass == null ? "unknow" : sceneClass.getSimpleName();
        String description = sceneWrapper.getDescription();
        return new Inner(conversationId, simpleName, description, type);
    }

    class Inner {

        private String conversationId;
        private String simpleName;
        private String description;
        private String type;

        public Inner(String conversationId, String simpleName, String description, String type) {
            this.conversationId = conversationId;
            this.simpleName = simpleName;
            this.description = description;
            this.type = type;
        }
    }
}

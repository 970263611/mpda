package com.dahuaboke.mpda.core.aop;


import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.context.CacheManager;
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

    @Pointcut("execution(* com.dahuaboke.mpda.core.agent.scene.SceneWrapper.apply(..)) || " +
            "execution(* com.dahuaboke.mpda.core.agent.scene.SceneWrapper.applyAsync(..))")
    public void sceneWrapperPointcut() {
    }

    @Before("sceneWrapperPointcut()")
    public void beforeExecute(JoinPoint joinPoint) {
        Inner inner = buildInner(joinPoint);
        logger.debug("{} >>> ({}) in >>> {}({})", inner.conversationId, inner.type, inner.simpleName, inner.description);
    }

    @AfterReturning("sceneWrapperPointcut()")
    public void afterReturnExecute(JoinPoint joinPoint) {
        Inner inner = buildInner(joinPoint);
        logger.debug("{} <<< ({}) out <<< {}({})", inner.conversationId, inner.type, inner.simpleName, inner.description);
    }

    @AfterThrowing("sceneWrapperPointcut()")
    public void afterThrowingExecute(JoinPoint joinPoint, Throwable throwable) {
        Inner inner = buildInner(joinPoint);
        logger.debug("{} !!! ({}) throw exception !!! {}({})", inner.conversationId, inner.type, inner.simpleName, inner.description, throwable);
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
        String simpleName = sceneWrapper.getSceneClass().getSimpleName();
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

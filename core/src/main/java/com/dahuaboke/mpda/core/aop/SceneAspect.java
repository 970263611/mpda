package com.dahuaboke.mpda.core.aop;


import com.dahuaboke.mpda.core.context.CacheManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/17 09:18
 */
@Aspect
@Component
public class SceneAspect {

    @Autowired
    private CacheManager cacheManager;

    @Pointcut("execution(* com.dahuaboke.mpda.core.agent.scene.SceneWrapper.apply(..)) || " +
            "execution(* com.dahuaboke.mpda.core.agent.scene.SceneWrapper.applyAsync(..))")
    public void sceneWrapperPointcut() {
    }

    @Before("sceneWrapperPointcut()")
    public void beforeExecute(JoinPoint joinPoint) {

    }
}

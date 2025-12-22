package com.dahuaboke.mpda.core.agent.prompt;


import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * auth: dahua
 * time: 2025/8/21 09:18
 */
@Aspect
@Component
public class AgentPromptProxy {

    private CacheManager cacheManager;
    private AgentPromptLoader agentPromptLoader;

    public AgentPromptProxy(CacheManager cacheManager, AgentPromptLoader agentPromptLoader) {
        this.cacheManager = cacheManager;
        this.agentPromptLoader = agentPromptLoader;
    }

    @Pointcut("execution(* com.dahuaboke.mpda.core.agent.prompt.AgentPrompt+.description(..))")
    public void agentPromptPointcut() {
    }

    @Around("agentPromptPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object targetObject = joinPoint.getTarget();
        Class<?> targetClass = targetObject.getClass();
        CoreContext context = cacheManager.getContext();
        String findStrategyName = context.getFindStrategy().name();
        String prompt = agentPromptLoader.extractPrompt(targetClass.getName(), findStrategyName);
        if (StringUtils.hasLength(prompt)) {
            return prompt;
        }
        return joinPoint.proceed();
    }
}

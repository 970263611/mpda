package com.dahuaboke.mpda.core.agent.prompt;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/21 09:18
 */
@Aspect
@Component
public class AgentPromptProxy {

    @Pointcut("within(com.dahuaboke.mpda.core.node..*)")
    public void agentPromptPointcut() {
    }

    private AgentPromptLoader agentPromptLoader;

    public AgentPromptProxy(AgentPromptLoader agentPromptLoader) {
        this.agentPromptLoader = agentPromptLoader;
    }


}

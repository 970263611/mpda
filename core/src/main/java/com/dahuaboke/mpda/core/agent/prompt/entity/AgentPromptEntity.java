package com.dahuaboke.mpda.core.agent.prompt.entity;


/**
 * auth: dahua
 * time: 2025/12/11 18:11
 */
public class AgentPromptEntity {

    private String name;
    private String prompt;
    private String sceneName;

    public AgentPromptEntity(String name, String prompt, String sceneName) {
        this.name = name;
        this.prompt = prompt;
        this.sceneName = sceneName;
    }

    public String getName() {
        return name;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getSceneName() {
        return sceneName;
    }
}

package com.dahuaboke.mpda.core.agent.prompt.entity;


/**
 * auth: dahua
 * time: 2025/12/11 18:11
 */
public class AgentPromptEntity {

    private String findStrategyName;
    private String prompt;
    private String sceneName;

    public AgentPromptEntity(String findStrategyName, String prompt, String sceneName) {
        this.findStrategyName = findStrategyName;
        this.prompt = prompt;
        this.sceneName = sceneName;
    }

    public String getFindStrategyName() {
        return findStrategyName;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}

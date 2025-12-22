package com.dahuaboke.mpda.core.agent.prompt.entity;


/**
 * auth: dahua
 * time: 2025/12/11 18:11
 */
public class AgentPromptEntity {

    private String findStrategyName;
    private String prompt;
    private String promptName;

    public AgentPromptEntity(String findStrategyName, String prompt, String promptName) {
        this.findStrategyName = findStrategyName;
        this.prompt = prompt;
        this.promptName = promptName;
    }

    public String getFindStrategyName() {
        return findStrategyName;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getPromptName() {
        return promptName;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}

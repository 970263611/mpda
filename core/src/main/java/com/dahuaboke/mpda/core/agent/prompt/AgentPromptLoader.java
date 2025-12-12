package com.dahuaboke.mpda.core.agent.prompt;


import com.dahuaboke.mpda.core.agent.prompt.entity.AgentPromptEntity;
import com.dahuaboke.mpda.core.agent.scene.strategy.SceneFinderManager;
import com.dahuaboke.mpda.core.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * auth: dahua
 * time: 2025/8/21 09:18
 */
@Component
public class AgentPromptLoader {

    private static final Logger logger = LoggerFactory.getLogger(AgentPromptLoader.class);
    private static final String CLASSPATH_PREFIX = "classpath:prompt/";
    private final List<AgentPromptEntity> agentPrompts = new ArrayList<>();
    private SceneFinderManager sceneFinderManager;

    public AgentPromptLoader(SceneFinderManager sceneFinderManager) {
        this.sceneFinderManager = sceneFinderManager;
    }

    public void load() {
        sceneFinderManager.strategyNames().stream().forEach(strategyName -> {
            try {
                List<String> fileNames = FileUtil.listAllFiles(CLASSPATH_PREFIX + strategyName);
                fileNames.forEach(fileName -> {
                    try {
                        String sceneName = fileName.split(strategyName)[1];
                        if (sceneName.startsWith("/")) {
                            sceneName = sceneName.substring(1);
                        }
                        if (sceneName.startsWith("\\")) {
                            sceneName = sceneName.substring(2);
                        }
                        agentPrompts.add(new AgentPromptEntity(strategyName, FileUtil.getFileAsString(fileName), sceneName));
                        logger.info("Load agent prompt file: {}", fileName);
                    } catch (IOException e) {
                        logger.error("Load agent prompt file failed, file name is {}", fileName, e);
                    }
                });
            } catch (IOException e) {
                logger.error("Load agent prompt file failed, dir name is {}", strategyName, e);
            }
        });
    }

    public String extractPrompt(String sceneName, String findStrategyName) {
        Stream<AgentPromptEntity> agentPromptEntityStream = agentPrompts.stream().filter(entity ->
                StringUtils.endsWithIgnoreCase(findStrategyName, entity.getFindStrategyName()) && StringUtils.endsWithIgnoreCase(sceneName, entity.getSceneName()));
        Optional<AgentPromptEntity> first = agentPromptEntityStream.findFirst();
        if (first.isPresent()) {
            return first.get().getPrompt();
        }
        return null;
    }

    public List<AgentPromptEntity> extractPrompt(String sceneName) {
        Stream<AgentPromptEntity> agentPromptEntityStream = agentPrompts.stream().filter(entity ->
                StringUtils.endsWithIgnoreCase(sceneName, entity.getSceneName()));
        return agentPromptEntityStream.toList();
    }

    public void updatePrompt(String sceneName, String findStrategyName, String prompt) {
        Stream<AgentPromptEntity> agentPromptEntityStream = agentPrompts.stream().filter(entity ->
                StringUtils.endsWithIgnoreCase(findStrategyName, entity.getFindStrategyName()) && StringUtils.endsWithIgnoreCase(sceneName, entity.getSceneName()));
        Optional<AgentPromptEntity> first = agentPromptEntityStream.findFirst();
        if (first.isPresent()) {
            first.get().setPrompt(prompt);
        }
    }
}

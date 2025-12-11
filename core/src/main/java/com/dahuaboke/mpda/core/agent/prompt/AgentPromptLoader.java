package com.dahuaboke.mpda.core.agent.prompt;


import com.dahuaboke.mpda.core.agent.prompt.entity.AgentPromptEntity;
import com.dahuaboke.mpda.core.agent.scene.strategy.SceneFinderManager;
import com.dahuaboke.mpda.core.utils.FileUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @PostConstruct
    public void load() {
        sceneFinderManager.strategyNames().stream().forEach(name -> {
            try {
                List<String> fileNames = FileUtil.listAllFiles(CLASSPATH_PREFIX + name);
                fileNames.forEach(fileName -> {
                    try {
                        agentPrompts.add(new AgentPromptEntity(name, FileUtil.getFileAsString(fileName), fileName));
                    } catch (IOException e) {
                        logger.error("Load agent prompt file failed, file name is {}", fileName, e);
                    }
                });
            } catch (IOException e) {
                logger.error("Load agent prompt file failed, dir name is {}", name, e);
            }
        });
    }
}

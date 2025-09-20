package com.dahuaboke.mpda.core.agent.scene;


import com.dahuaboke.mpda.core.agent.chain.DefaultChain;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.exception.MpdaIllegalConfigException;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;

/**
 * auth: dahua
 * time: 2025/8/21 14:41
 */
@Component
public class SceneManager implements BeanPostProcessor {

    private final List<Scene> scenes = new ArrayList<>();
    private final Map<String, SceneWrapper> sceneWrappers = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    private CacheManager cacheManager;
    private SceneWrapper rootWrapper;
    private boolean isInit = false;
    private UnknowWrapper unknowWrapper;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Scene scene) {
            SceneWrapper wrapper;
            if (scene.parent() == null) {
                if (rootWrapper != null) {
                    throw new MpdaIllegalConfigException("Root scene only one");
                }
                rootWrapper = buildWrapper(scene);
                wrapper = rootWrapper;
            } else {
                scenes.add(scene);
                wrapper = buildWrapper(scene);
            }
            sceneWrappers.put(scene.getClass().getSimpleName(), wrapper);
        }
        if (bean instanceof CacheManager cacheManager) {
            this.cacheManager = cacheManager;
        }
        if (bean instanceof UnknowWrapper unknowWrapper) {
            this.unknowWrapper = unknowWrapper;
        }
        return bean;
    }

    public void lazyInit() {
        scenes.stream().forEach(scene -> {
            Class<? extends Scene> parent = scene.parent();
            Scene parentScene = SpringUtil.getBean(parent);
            SceneWrapper parentSceneWrapper = sceneWrappers.get(parentScene.getClass().getSimpleName());
            SceneWrapper childSceneWrapper = sceneWrappers.get(scene.getClass().getSimpleName());
            parentSceneWrapper.addChildWrapper(childSceneWrapper);
        });
        sceneWrappers.values().stream().forEach(sceneWrapper -> {
            try {
                cacheManager.addScenedWrapper(sceneWrapper.getSceneId(), sceneWrapper);
                sceneWrapper.init();
            } catch (MpdaGraphException e) {
                e.printStackTrace(); //TODO
            }
        });
        isInit = true;
    }

    private SceneWrapper buildWrapper(Scene scene) {
        DefaultChain chain = DefaultChain.builder()
                .graph(scene.graph())
                .prompt(scene.prompt())
                .cacheManager(cacheManager)
                .build();
        SceneWrapper wrapper = SceneWrapper.builder()
                .chain(chain)
                .scene(scene)
                .build();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(SceneWrapper.class).getBeanDefinition();
        beanDefinition.setScope("prototype");
        beanDefinition.setInstanceSupplier(() -> wrapper);
        registry.registerBeanDefinition(wrapper.getSceneId(), beanDefinition);
        return applicationContext.getBean(wrapper.getSceneId(), SceneWrapper.class);
    }

    public SceneResponse apply(CoreContext context) throws MpdaException {
        SceneWrapper sceneWrapper = next(context);
        try {
            context.setSceneId(sceneWrapper.getSceneId());
            cacheManager.setContext(context);
            return sceneWrapper.apply(context);
        } finally {
            cacheManager.removeContext();
        }
    }

    public Flux<SceneResponse> applyAsync(CoreContext context) throws MpdaException {
        SceneWrapper sceneWrapper = next(context);
        try {
            context.setSceneId(sceneWrapper.getSceneId());
            cacheManager.setContext(context);
            return sceneWrapper.applyAsync(context);
        } finally {
            cacheManager.removeContext();
        }
    }

    private SceneWrapper next(CoreContext context) throws MpdaException {
        if (!isInit) {
            lazyInit();
        }
        SceneWrapper runtimeWrapper = rootWrapper;
        while (!runtimeWrapper.isEnd()) {
            try {
                context.setSceneId(runtimeWrapper.getSceneId());
                cacheManager.setContext(context);
                runtimeWrapper = next(context, runtimeWrapper);
            } finally {
                cacheManager.removeContext();
            }
        }
        return runtimeWrapper;
    }

    private SceneWrapper next(CoreContext context, SceneWrapper runtimeWrapper) throws MpdaException {
        return next(context, runtimeWrapper, 0);
    }

    private SceneWrapper next(CoreContext context, SceneWrapper runtimeWrapper, int retry) throws MpdaException {
        SceneResponse execute = runtimeWrapper.apply(context);
        String output = execute.output();
        if (output.startsWith("<think>")) {
            output = output.replaceFirst("(?s)<think>.*?</think>", "");
        }
        String finalExecute = output.trim();
        Set<SceneWrapper> childrenWrapper = runtimeWrapper.getChildrenWrapper();
        Optional<SceneWrapper> match = childrenWrapper.stream().filter(child -> child.getSceneId().equals(finalExecute)).findFirst();
        if (match.isPresent()) {
            return match.get();
        }
        retry++;
        if (retry >= 3) {
            return unknowWrapper;
        }
        return next(context, runtimeWrapper, retry);
    }
}

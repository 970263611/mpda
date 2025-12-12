package com.dahuaboke.mpda.core.context;

import com.dahuaboke.mpda.core.agent.chain.DefaultChain;
import com.dahuaboke.mpda.core.agent.prompt.AgentPromptLoader;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.exception.MpdaIllegalConfigException;
import com.dahuaboke.mpda.core.memory.MemoryExclude;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * auth: dahua
 * time: 2025/9/21 13:30
 */
@Component
public class CacheManager implements BeanPostProcessor {

    private final Map<String, SceneWrapper> sceneWrappers = new HashMap<>();
    private final List<Scene> scenes = new ArrayList<>();
    /**
     * memory
     */
    private final Map<String, Map<String, LimitedListWrapper<Message>>> memories = new LinkedHashMap();
    /**
     * trace
     */
    private final Map<String, LimitedListWrapper<TraceMessage>> traces = new LinkedHashMap();
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AgentPromptLoader agentPromptLoader;
    /**
     * scene
     */
    private SceneWrapper rootWrapper;
    /**
     * thread local
     */
    private ThreadLocal<CoreContext> contextThreadLocal = new ThreadLocal<>();
    private ThreadLocal<Map<String, Object>> attributeThreadLocal = new ThreadLocal<>();
    private ThreadLocal<String> promptFromFieThreadLocal = new ThreadLocal<>();

    public CoreContext getContext() {
        return contextThreadLocal.get();
    }

    public void setContext(CoreContext context) {
        contextThreadLocal.set(context);
    }

    public void removeContext() {
        contextThreadLocal.remove();
    }

    public String getPromptFromFie() {
        return promptFromFieThreadLocal.get();
    }

    public void setPromptFromFie(String prompt) {
        promptFromFieThreadLocal.set(prompt);
    }

    public void removePromptFromFie() {
        promptFromFieThreadLocal.remove();
    }

    public Map<String, Object> getAttribute() {
        try {
            return attributeThreadLocal.get();
        } finally {
            removeAttribute();
        }
    }

    public void setAttribute(Map<String, Object> attribute) {
        attributeThreadLocal.set(attribute);
    }

    public void removeAttribute() {
        attributeThreadLocal.remove();
    }

    public String getSceneNameBySceneClass(Class<? extends Scene> clz) {
        return sceneWrappers.values().stream().filter(
                wrapper -> wrapper.getSceneName().equals(clz.getName())).findFirst().orElseThrow(MpdaIllegalConfigException::new).getSceneName();
    }

    public Map<String, Map<String, LimitedListWrapper<Message>>> getMemories() {
        return memories;
    }

    public Map<String, LimitedListWrapper<TraceMessage>> getTraces() {
        return traces;
    }

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
            sceneWrappers.put(wrapper.getSceneName(), wrapper);
        }
        return bean;
    }

    private SceneWrapper buildWrapper(Scene scene) {
        MemoryExclude memoryExclude = scene.getClass().getAnnotation(MemoryExclude.class);
        Set<Class<? extends Message>> memoryExcludeSet = new HashSet<>();
        if (memoryExclude != null) {
            Class<? extends Message>[] classes = memoryExclude.value();
            if (classes != null) {
                memoryExcludeSet.addAll(Arrays.asList(classes));
            }
        }
        DefaultChain chain = DefaultChain.builder()
                .graph(scene.graph())
                .prompt(scene.prompt())
                .cacheManager(this)
                .memoryExclude(memoryExcludeSet)
                .build();
        SceneWrapper wrapper = SceneWrapper.builder()
                .chain(chain)
                .scene(scene)
                .agentPromptLoader(agentPromptLoader)
                .cacheManager(this)
                .build();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(SceneWrapper.class).getBeanDefinition();
        beanDefinition.setScope("prototype");
        beanDefinition.setInstanceSupplier(() -> wrapper);
        registry.registerBeanDefinition(wrapper.getSceneName(), beanDefinition);
        return applicationContext.getBean(wrapper.getSceneName(), SceneWrapper.class);
    }

    public SceneWrapper getRootWrapper() {
        return rootWrapper;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public Map<String, SceneWrapper> getSceneWrappers() {
        return sceneWrappers;
    }

    public SceneWrapper getSceneWrapperByName(String sceneName) {
        return sceneWrappers.get(sceneName);
    }
}

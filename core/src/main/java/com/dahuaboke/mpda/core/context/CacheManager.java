package com.dahuaboke.mpda.core.context;

import com.dahuaboke.mpda.core.agent.chain.DefaultChain;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.exception.MpdaIllegalConfigException;
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

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * scene
     */
    private SceneWrapper rootWrapper;
    private final Map<String, SceneWrapper> sceneWrappers = new HashMap<>();
    private final Map<String, SceneWrapper> sceneNameWrappers = new HashMap<>();
    private final List<Scene> scenes = new ArrayList<>();
    /**
     * memory
     */
    private final Map<String, Map<String, LimitedListWrapper<Message>>> memories = new LinkedHashMap();
    /**
     * trace
     */
    private final Map<String, LimitedListWrapper<TraceMessage>> traces = new LinkedHashMap();
    /**
     * thread local
     */
    private ThreadLocal<CoreContext> contextThreadLocal = new ThreadLocal<>();
    private ThreadLocal<Map<String, Object>> attributeThreadLocal = new ThreadLocal<>();

    public CoreContext getContext() {
        return contextThreadLocal.get();
    }

    public void setContext(CoreContext context) {
        contextThreadLocal.set(context);
    }

    public void removeContext() {
        contextThreadLocal.remove();
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

    public void addScenedWrapper(String sceneId, SceneWrapper sceneWrapper) {
        sceneWrappers.put(sceneId, sceneWrapper);
    }

    public Map<String, SceneWrapper> getSceneWrappers() {
        return sceneWrappers;
    }

    public String getSceneIdBySceneClass(Class<? extends Scene> clz) {
        return sceneWrappers.values().stream().filter(
                wrapper -> wrapper.getSceneClass().equals(clz)).findFirst().orElseThrow(MpdaIllegalConfigException::new).getSceneId();
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
            sceneNameWrappers.put(scene.getClass().getSimpleName(), wrapper);
        }
        return bean;
    }

    private SceneWrapper buildWrapper(Scene scene) {
        DefaultChain chain = DefaultChain.builder()
                .graph(scene.graph())
                .prompt(scene.prompt())
                .cacheManager(this)
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

    public SceneWrapper getRootWrapper() {
        return rootWrapper;
    }

    public Map<String, SceneWrapper> getSceneNameWrappers() {
        return sceneNameWrappers;
    }

    public List<Scene> getScenes() {
        return scenes;
    }
}

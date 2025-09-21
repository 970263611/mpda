package com.dahuaboke.mpda.core.event;

import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

/**
 * @author dahua
 * @time 2025/7/15
 */
@Component
public class ListenerRegister implements BeanPostProcessor {

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Listener) {
            ResolvableType resolvableType = ResolvableType.forClass(bean.getClass()).as(Listener.class);
            ResolvableType eventType = resolvableType.getGeneric(0);
            Class<?> eventClass = eventType.resolve();
            Class<? extends Event> jmanusEventClass;
            try {
                jmanusEventClass = (Class<? extends Event>) eventClass;
            } catch (Exception e) {
                throw new MpdaIllegalArgumentException("The listener can only listen to event type");
            }
            eventPublisher.registerListener(jmanusEventClass, (Listener) bean);
        }
        return bean;
    }

}

package com.dahuaboke.mpda.bot.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * @description: 1
 * @author: ZHANGSHUHAN
 * @date: 2025/11/19
 */

@Component
public class AllConfigPrinter {
    private final ConfigurableEnvironment environment;

    public AllConfigPrinter(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printAllConfigs() {
        System.out.println("===项目起动后生效的所有YAML配置");
        Map<String, String> allConfigs = new TreeMap<>();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource<?> enumerableSource) {
                for (String key : enumerableSource.getPropertyNames()) {
                    String value = environment.getProperty(key);
                    allConfigs.put(key, value);
                }
            }
        }
        allConfigs.forEach((key, value) -> System.out.printf("%-40s = %s%n", key, value));
        System.out.println("==================");
    }
}

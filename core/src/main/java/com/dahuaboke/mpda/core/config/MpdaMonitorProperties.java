package com.dahuaboke.mpda.core.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * auth: dahua
 * time: 2025/12/11 15:23
 */
@ConfigurationProperties(prefix = "mpda.monitor")
public class MpdaMonitorProperties {

    public static final String PERSISTENCE_ENABLE = "mpda.monitor.persistence.enabled";
    public static final String PERSISTENCE_ENABLE_VALUE = "true";

    private Persistence persistence;

    public Persistence getPersistence() {
        return persistence;
    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    class Persistence {
        private int max = 10000;

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }
}

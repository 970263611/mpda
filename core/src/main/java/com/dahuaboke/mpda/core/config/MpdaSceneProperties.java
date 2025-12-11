package com.dahuaboke.mpda.core.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * auth: dahua
 * time: 2025/12/11 15:22
 */
@ConfigurationProperties(prefix = "mpda.scene")
public class MpdaSceneProperties {

    private int retry;

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}

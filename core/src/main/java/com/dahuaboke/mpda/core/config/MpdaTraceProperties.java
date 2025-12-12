package com.dahuaboke.mpda.core.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * auth: dahua
 * time: 2025/12/11 15:24
 */
@ConfigurationProperties(prefix = "mpda.trace")
public class MpdaTraceProperties {

    private int max = 100;
    private int timeout = 10; // minute
    private int check = 30; // second

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}

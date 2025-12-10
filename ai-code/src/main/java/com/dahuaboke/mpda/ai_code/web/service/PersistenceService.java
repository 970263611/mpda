package com.dahuaboke.mpda.ai_code.web.service;


import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/12/10 10:48
 */
@Component
public class PersistenceService extends com.dahuaboke.mpda.core.monitor.persistence.PersistenceService {

    public PersistenceService() {
        super(10000);
    }
}

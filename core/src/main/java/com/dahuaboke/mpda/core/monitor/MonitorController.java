package com.dahuaboke.mpda.core.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 2025/9/22 9:28
 * auth: dahua
 * desc:
 */
@RestController
public class MonitorController {

    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private MonitorManager monitorManager;

    @RequestMapping(value = "/monitor", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=gbk")
    public Flux monitor() {
        return monitorManager.monitor();
    }
}

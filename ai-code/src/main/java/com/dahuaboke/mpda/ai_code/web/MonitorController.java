package com.dahuaboke.mpda.ai_code.web;


import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.monitor.persistence.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/12/9 11:13
 */
@RestController("aiCodeMonitorController")
@CrossOrigin
public class MonitorController {

    @Autowired
    private PersistenceManager persistenceManager;

    @RequestMapping(value = "/monitor-sse-db", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=gbk")
    public Flux<Event> monitorFromDb(@RequestParam("begin") Long begin, @RequestParam("end") Long end) {
        return persistenceManager.monitorStream(begin, end);
    }
}

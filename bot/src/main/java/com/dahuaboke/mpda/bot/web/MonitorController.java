package com.dahuaboke.mpda.bot.web;


import com.dahuaboke.mpda.bot.monitor.MonitorService;
import com.dahuaboke.mpda.core.event.Event;
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
@RestController
@CrossOrigin
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/monitor-sse-db", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=gbk")
    public Flux<Event> monitorFromDb(@RequestParam("begin") String begin, @RequestParam("end") String end) {
        return monitorService.getMonitorEvent(begin, end);
    }
}

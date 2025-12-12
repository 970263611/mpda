package com.dahuaboke.mpda.core.monitor;

import com.dahuaboke.mpda.core.monitor.persistence.PersistenceManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Flux;

/**
 * 2025/9/22 9:28
 * auth: dahua
 * desc:
 */
@Controller
@CrossOrigin
public class MonitorController implements WebMvcConfigurer {

    @Autowired
    private MonitorManager monitorManager;
    @Autowired
    private PersistenceManager persistenceManager;

    @RequestMapping(value = "/monitor-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=gbk")
    @ResponseBody
    public Flux monitorSse(HttpServletRequest request) {
        String begin = request.getParameter("begin");
        String end = request.getParameter("end");
        if (begin != null && end != null) {
            try {
                return persistenceManager.monitorStream(Long.parseLong(begin), Long.parseLong(end));
            } catch (NumberFormatException e) {
            }
        }
        return monitorManager.monitor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/views/**")
                .addResourceLocations("classpath:/views/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/monitor")
                .setViewName("forward:/views/monitor.html");
        registry.addViewController("/monitor.html")
                .setViewName("forward:/views/monitor.html");
    }
}

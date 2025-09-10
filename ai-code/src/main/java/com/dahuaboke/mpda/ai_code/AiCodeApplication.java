package com.dahuaboke.mpda.ai_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * auth: dahua
 * time: 2025/8/21 14:37
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.dahuaboke.*")
public class AiCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeApplication.class, args);
    }
}

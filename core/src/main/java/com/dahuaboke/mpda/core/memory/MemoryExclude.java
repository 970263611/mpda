package com.dahuaboke.mpda.core.memory;


import org.springframework.ai.chat.messages.Message;

import java.lang.annotation.*;

/**
 * auth: dahua
 * time: 2025/11/13 19:34
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemoryExclude {

    Class<? extends Message>[] value();
}

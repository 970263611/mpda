package com.dahuaboke.mpda.core.memory;

import com.dahuaboke.mpda.core.agent.scene.Scene;

import java.lang.annotation.*;

/**
 * auth: dahua
 * time: 2025/9/20 11:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemoryMerge {

    Class<? extends Scene>[] value();
}

package com.dahuaboke.mpda.bot.utils;

import java.lang.annotation.*;

/**
 * 字段中文翻译注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldTranslation {
    /**
     * 字段的中文翻译
     */
    String value();

    /**
     * 字段排序顺序（数值越小越靠前，默认0）
     */
    int order() default 0;
}
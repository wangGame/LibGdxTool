package com.libGdx.test.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodTime {

    /**
     * 自定义显示名称。
     * 为空时使用“类名#方法名”。
     */
    String value() default "";

    /**
     * 单方法耗时阈值。
     * 小于 0 时使用全局阈值。
     */
    long thresholdMs() default -1;
}
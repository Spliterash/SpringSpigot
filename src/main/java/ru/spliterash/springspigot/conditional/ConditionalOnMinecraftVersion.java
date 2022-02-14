package ru.spliterash.springspigot.conditional;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Создаст бин только если версия между указаными либо больше минимальной
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnVersionConditional.class)
public @interface ConditionalOnMinecraftVersion {
    String min() default "";

    String max() default "";
}

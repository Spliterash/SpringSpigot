package ru.spliterash.springspigot.listener;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// Я чот так подумал, особо смысла нет, можно же просто листер на instanceof чекать
@Deprecated
@Component
public @interface SpigotListener {
    /**
     * Это имя бина
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}

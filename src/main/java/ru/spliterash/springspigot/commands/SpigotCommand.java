package ru.spliterash.springspigot.commands;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Класс обязан быть {@link org.bukkit.command.CommandExecutor}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SpigotCommand {
    /**
     * Это имя бина
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * Название команды в plugin yml
     */
    String command();
}

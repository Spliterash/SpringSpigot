package ru.spliterash.springspigot.schedule.annotation;

import java.lang.annotation.*;

/**
 * Если повесить это на метод, то запустит асинхронный шедулер на этот метод
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncSchedule {
    /**
     * Время перед вызовами метода
     */
    long period();
}

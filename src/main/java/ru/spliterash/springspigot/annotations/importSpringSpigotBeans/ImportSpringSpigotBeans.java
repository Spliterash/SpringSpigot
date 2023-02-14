package ru.spliterash.springspigot.annotations.importSpringSpigotBeans;

import org.springframework.context.annotation.Import;
import ru.spliterash.springspigot.init.SpringSpigotPlugin;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ImportSpringSpigotBeansRegistar.class)
@Repeatable(ImportMultipleSpringSpigotBeans.class)
public @interface ImportSpringSpigotBeans {
    Class<? extends SpringSpigotPlugin> plugin() default SpringSpigotPlugin.class;

    Class<?>[] beans();
}

package ru.spliterash.springspigot.init;

import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import ru.spliterash.springspigot.SpringSpigotPlugin;
import ru.spliterash.springspigot.common.CompoundClassLoader;

import java.util.HashSet;
import java.util.Set;

public final class SpringSpigotBootstrapper {

    private SpringSpigotBootstrapper() {
    }

    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, Class<?> appClass) {
        if (plugin.getClass().equals(appClass))
            throw new RuntimeException("Plugin can be app class");

        Set<ClassLoader> loaders = new HashSet<>();

        loaders.add(plugin.getClass().getClassLoader());
        loaders.add(SpringSpigotPlugin.class.getClassLoader());
        loaders.add(Thread.currentThread().getContextClassLoader());

        CompoundClassLoader classLoader = new CompoundClassLoader(loaders);

        return initialize(plugin, classLoader, appClass);
    }


    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, ClassLoader classLoader, Class<?> appClass) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(appClass);

        builder
                .application()
                .setMainApplicationClass(appClass);

        return builder
                .resourceLoader(new DefaultResourceLoader(classLoader))
                .initializers(new SpringSpigotInitializer(plugin))
                .run();
    }


}

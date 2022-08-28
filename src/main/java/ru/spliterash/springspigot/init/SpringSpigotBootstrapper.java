package ru.spliterash.springspigot.init;

import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import ru.spliterash.springspigot.LibSpringSpigotPlugin;

import java.util.ArrayList;
import java.util.List;
@EnableAutoConfiguration()
public final class SpringSpigotBootstrapper {

    private SpringSpigotBootstrapper() {
    }

    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, Class<?> appClass) {
        if (plugin.getClass().equals(appClass))
            throw new RuntimeException("Plugin cant be app class");

        List<ClassLoader> loaders = new ArrayList<>(4);

        // Плагин имеет высший приоритет
        loaders.add(plugin.getClass().getClassLoader());
        // Потом идёт сам спринг
        loaders.add(LibSpringSpigotPlugin.class.getClassLoader());
        // А эт я понятия не имею чо такое
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
                .bannerMode(Banner.Mode.OFF)
                .run();
    }


}

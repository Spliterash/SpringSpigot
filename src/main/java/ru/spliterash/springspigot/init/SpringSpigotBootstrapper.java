package ru.spliterash.springspigot.init;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import ru.spliterash.springspigot.configuration.ConfigurationPropertySource;

import java.util.Properties;

@UtilityClass
public final class SpringSpigotBootstrapper {
    public static ConfigurableApplicationContext initialize(JavaPlugin plugin, String... scanPackages) {
        String[] finalPackages = new String[scanPackages.length + 1];
        System.arraycopy(scanPackages, 0, finalPackages, 0, scanPackages.length);
        finalPackages[finalPackages.length - 1] = "ru.spliterash.springspigot";
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(finalPackages);
        context.setClassLoader(plugin.getClass().getClassLoader());
        init(context, plugin);

        context.start();

        return context;
    }

    private void init(ConfigurableApplicationContext context, JavaPlugin plugin) {
        val propertySources = context.getEnvironment().getPropertySources();
        propertySources.addLast(new ConfigurationPropertySource(plugin.getConfig()));

        val props = new Properties();
        props.put("spigot.plugin", plugin.getName());
        propertySources.addLast(new PropertiesPropertySource("spring-bukkit", props));
    }
}

package ru.spliterash.springspigot.init;

import lombok.val;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import ru.spliterash.springspigot.SpringSpigotConfiguration;
import ru.spliterash.springspigot.configuration.ConfigurationPropertySource;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Initializer that set core properties and adds config yml source
 */
public class SpringSpigotInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final Plugin plugin;

    public SpringSpigotInitializer(Plugin plugin) {
        this.plugin = plugin;
    }

    public void initialize(ConfigurableApplicationContext context) {
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();

        InputStream applicationYamlInput = plugin.getResource("application.yml");

        if (applicationYamlInput != null) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(applicationYamlInput));
            ConfigurationPropertySource innerPluginConfig = new ConfigurationPropertySource(configuration);

            propertySources.addLast(innerPluginConfig);

            String springConfig = configuration.getString("spring-spigot.config");

            if (springConfig != null) {
                plugin.saveResource(springConfig, false);

                File springPluginConfig = new File(plugin.getDataFolder(), springConfig);
                if (springPluginConfig.isFile()) {

                    YamlConfiguration pluginSpringConfiguration = YamlConfiguration.loadConfiguration(springPluginConfig);
                    propertySources.addLast(new ConfigurationPropertySource(pluginSpringConfiguration));
                }
            }
        }
        context.getBeanFactory().registerSingleton(plugin.getClass().getCanonicalName(), plugin);

        if (context instanceof AnnotationConfigApplicationContext) {
            ((AnnotationConfigApplicationContext) context).register(SpringSpigotConfiguration.class);
        }
    }

}
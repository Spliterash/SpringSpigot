package ru.spliterash.springspigot.configuration;

import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntellijIdeaConfiguration {
    @Bean
    public JavaPlugin javaPlugin() {
        return null;
    }
}

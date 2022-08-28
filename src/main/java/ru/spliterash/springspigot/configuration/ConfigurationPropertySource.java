package ru.spliterash.springspigot.configuration;

import org.bukkit.configuration.Configuration;
import org.springframework.core.env.PropertySource;

public class ConfigurationPropertySource extends PropertySource<Configuration> {

    public ConfigurationPropertySource(Configuration source) {
        super("config", source);
    }

    @Override
    public Object getProperty(String s) {
        return source.get(s);
    }
}

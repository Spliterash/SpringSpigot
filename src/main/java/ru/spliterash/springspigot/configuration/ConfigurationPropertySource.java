package ru.spliterash.springspigot.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.springframework.core.env.PropertySource;

public class ConfigurationPropertySource extends PropertySource<FileConfiguration> {

    public ConfigurationPropertySource(FileConfiguration source) {
        super("config", source);
    }

    @Override
    public Object getProperty(String s) {
        return source.get(s);
    }
}

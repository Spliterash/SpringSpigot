package ru.spliterash.springspigot.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.springspigot.annotations.IgnoreDuringScan;

@Configuration
@ComponentScan(basePackages = "ru.spliterash.springspigot", excludeFilters = {
        @ComponentScan.Filter(IgnoreDuringScan.class)
})
public class SpringSpigotConfiguration {

}

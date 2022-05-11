package ru.spliterash.springspigot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SpigotCommandInitializer implements BeanPostProcessor {
    private final JavaPlugin plugin;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof CommandExecutor) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            SpigotCommand annotation = targetClass.getAnnotation(SpigotCommand.class);
            if (annotation != null) {
                String commandValue = annotation.command();

                PluginCommand cmd = plugin.getCommand(commandValue);

                if (cmd != null)
                    cmd.setExecutor((CommandExecutor) bean);
                else
                    log.warn("You forgot add command '" + commandValue + "' to plugin.yml");

            }
        }

        return bean;
    }
}

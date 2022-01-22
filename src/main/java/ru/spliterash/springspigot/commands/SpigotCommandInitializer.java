package ru.spliterash.springspigot.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Collection;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SpigotCommandInitializer {
    private final JavaPlugin plugin;
    private final ApplicationContext context;

    private boolean initialized = false;

    @SuppressWarnings("unused")
    @EventListener
    public void onStartup(ContextRefreshedEvent event) {
        if (initialized) return;
        initialized = true;

        Collection<Object> commands = context.getBeansWithAnnotation(SpigotCommand.class)
                .values();

        for (Object command : commands) {
            if (!(command instanceof CommandExecutor)) {
                log.warn("@SpringCommand class '" + command.getClass().getSimpleName() + "' is not CommandExecutor");
                continue;
            }

            SpigotCommand annotation = AopUtils.getTargetClass(command).getAnnotation(SpigotCommand.class);
            String commandValue = annotation.command();

            PluginCommand cmd = plugin.getCommand(commandValue);
            if (cmd == null) {
                log.warn("You forgot add command '" + commandValue + "' to plugin.yml");
                continue;
            }

            cmd.setExecutor((CommandExecutor) command);
        }
    }
}

package ru.spliterash.springspigot.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ru.spliterash.springspigot.commands.SpigotCommand;

import java.util.Collection;

@Slf4j
@Configuration
@ConditionalOnClass({Bukkit.class})
@RequiredArgsConstructor
public class SpringSpigotConfiguration {
    private final JavaPlugin plugin;
    private final ApplicationContext context;

    private boolean initialized = false;

    @EventListener
    public void onStartup() {
        if (initialized) return;
        initialized = true;
        // Регаем команды
        registerCommands();
        // Евентики
        registerEvents();
    }

    @EventListener
    public void onClose() {
        HandlerList.unregisterAll(plugin);
    }


    private void registerCommands() {
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

    private void registerEvents() {
        for (Listener value : context.getBeansOfType(Listener.class).values()) {
            plugin.getServer().getPluginManager().registerEvents(value, plugin);
        }
    }
}

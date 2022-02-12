package ru.spliterash.springspigot.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ru.spliterash.springspigot.utils.ProxyUtils;

@Configuration
@RequiredArgsConstructor
public class SpigotListenerInitializer {
    private final JavaPlugin plugin;
    private final ApplicationContext context;

    private boolean initialized = false;

    @SuppressWarnings("unused")
    @EventListener
    public void onStartup(ContextRefreshedEvent event) {
        if (initialized)
            return;
        initialized = true;

        for (Listener value : context.getBeansOfType(Listener.class).values()) {
            Listener realListener = ProxyUtils.getProxyTarget(value);
            plugin.getServer().getPluginManager().registerEvents(realListener, plugin);
        }
    }

    @EventListener
    public void onClose(@SuppressWarnings("unused") ContextClosedEvent event) {
        HandlerList.unregisterAll(plugin);
    }
}

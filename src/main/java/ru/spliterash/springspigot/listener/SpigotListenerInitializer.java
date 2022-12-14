package ru.spliterash.springspigot.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.springspigot.utils.ProxyUtils;

@Configuration
@RequiredArgsConstructor
public class SpigotListenerInitializer implements DestructionAwareBeanPostProcessor {
    private final JavaPlugin plugin;

    @Nullable
    private Listener getListener(Object bean) {
        if (bean instanceof Listener && bean.getClass().isAnnotationPresent(SpigotListener.class)) {
            return ProxyUtils.getProxyTarget(bean);
        }

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Listener listener = getListener(bean);
        if (listener != null)
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);

        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        Listener listener = getListener(bean);
        if (listener != null)
            HandlerList.unregisterAll(listener);
    }
}

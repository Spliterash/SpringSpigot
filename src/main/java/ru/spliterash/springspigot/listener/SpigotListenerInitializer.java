package ru.spliterash.springspigot.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.springspigot.utils.ProxyUtils;

import javax.annotation.PreDestroy;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SpigotListenerInitializer implements DestructionAwareBeanPostProcessor {
    private final JavaPlugin plugin;

    @PreDestroy
    public void onClose() {
        HandlerList.unregisterAll(plugin);
    }

    private Optional<Listener> getListener(Object bean) {
        if (bean instanceof Listener) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            if (targetClass.isAnnotationPresent(SpigotListener.class)) {
                Listener realListener = ProxyUtils.getProxyTarget(bean);

                return Optional.of(realListener);
            }
        }

        return Optional.empty();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        getListener(bean)
                .ifPresent(l -> plugin.getServer().getPluginManager().registerEvents(l, plugin));

        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        getListener(bean)
                .ifPresent(HandlerList::unregisterAll);
    }
}

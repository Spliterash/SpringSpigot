package ru.spliterash.springspigot.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.springspigot.utils.ProxyUtils;

import javax.annotation.PreDestroy;

@Configuration
@RequiredArgsConstructor
public class SpigotListenerInitializer implements BeanPostProcessor {
    private final JavaPlugin plugin;

    @PreDestroy
    public void onClose() {
        HandlerList.unregisterAll(plugin);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Listener) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            if (targetClass.isAnnotationPresent(SpigotListener.class)) {
                Listener realListener = ProxyUtils.getProxyTarget(bean);
                plugin.getServer().getPluginManager().registerEvents(realListener, plugin);
            }
        }

        return bean;
    }
}

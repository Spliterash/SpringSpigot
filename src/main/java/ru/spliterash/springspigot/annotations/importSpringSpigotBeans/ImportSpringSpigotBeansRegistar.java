package ru.spliterash.springspigot.annotations.importSpringSpigotBeans;

import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import ru.spliterash.springspigot.init.SpringSpigotPlugin;

import java.util.Map;
import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE)
class ImportSpringSpigotBeansRegistar implements ImportBeanDefinitionRegistrar {
    @SuppressWarnings("unchecked")
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        SingletonBeanRegistry castedRegistry = (SingletonBeanRegistry) registry;

        Map<String, Object> attrs = Objects.requireNonNull(metadata.getAnnotationAttributes(ImportSpringSpigotBeans.class.getName()));

        Class<? extends SpringSpigotPlugin> pluginClass = (Class<? extends SpringSpigotPlugin>) attrs.get("plugin");
        Class<?>[] needleBeans = (Class<?>[]) attrs.get("beans");

        SpringSpigotPlugin plugin = JavaPlugin.getPlugin(pluginClass);


        for (Class<?> needleBean : needleBeans) {
            String beanName = plugin.getName() + "." + needleBean.getCanonicalName();
            Object bean = plugin.getService(needleBean);

            castedRegistry.registerSingleton(beanName, bean);
        }
    }
}
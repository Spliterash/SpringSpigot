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

@Order(Ordered.HIGHEST_PRECEDENCE)
class ImportSpringSpigotBeansRegistar implements ImportBeanDefinitionRegistrar {
    @SuppressWarnings("unchecked")
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        SingletonBeanRegistry castedRegistry = (SingletonBeanRegistry) registry;

        Map<String, Object> singleBean = metadata.getAnnotationAttributes(ImportSpringSpigotBeans.class.getName());
        if (singleBean != null) {
            Class<? extends SpringSpigotPlugin> pluginClass = (Class<? extends SpringSpigotPlugin>) singleBean.get("plugin");
            Class<?>[] beans = (Class<?>[]) singleBean.get("beans");

            importPlugin(castedRegistry, pluginClass, beans);
        }

        Map<String, Object> multipleBeans = metadata.getAnnotationAttributes(ImportMultipleSpringSpigotBeans.class.getName());
        if (multipleBeans != null) {
            ImportSpringSpigotBeans[] value = (ImportSpringSpigotBeans[]) multipleBeans.get("value");
            for (ImportSpringSpigotBeans annotation : value) {
                importPlugin(castedRegistry, annotation.plugin(), annotation.beans());
            }
        }
    }

    private void importPlugin(SingletonBeanRegistry registry, Class<? extends SpringSpigotPlugin> pluginClass, Class<?>[] needleBeans) {
        SpringSpigotPlugin plugin = JavaPlugin.getPlugin(pluginClass);


        for (Class<?> needleBean : needleBeans) {
            String beanName = plugin.getName() + "." + needleBean.getCanonicalName();
            Object bean = plugin.getService(needleBean);

            registry.registerSingleton(beanName, bean);
        }
    }
}
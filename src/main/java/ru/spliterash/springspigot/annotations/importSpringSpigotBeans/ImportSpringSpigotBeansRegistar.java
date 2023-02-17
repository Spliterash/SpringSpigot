package ru.spliterash.springspigot.annotations.importSpringSpigotBeans;

import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import ru.spliterash.springspigot.init.SpringSpigotPlugin;

import java.util.Map;

@Log4j2
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
            AnnotationAttributes[] value = (AnnotationAttributes[]) multipleBeans.get("value");

            for (AnnotationAttributes annotation : value) {
                Class<? extends SpringSpigotPlugin> plugin = annotation.getClass("plugin");
                Class<?>[] beans = annotation.getClassArray("beans");

                importPlugin(castedRegistry, plugin, beans);
            }
        }
    }

    private void importPlugin(SingletonBeanRegistry registry, Class<? extends SpringSpigotPlugin> pluginClass, Class<?>[] needleBeans) {
        JavaPlugin plugin;

        if (pluginClass.equals(SpringSpigotPlugin.class)) {
            try {
                plugin = JavaPlugin.getProvidingPlugin(needleBeans[0]);
            } catch (Exception ex) {
                log.warn("Failed import spring spigot beans, because plugin not found using class " + needleBeans[0].getName());
                return;
            }
        } else {
            try {
                plugin = JavaPlugin.getPlugin(pluginClass);
            } catch (Exception exception) {
                log.warn("Failed find plugin by class " + pluginClass.getName());
                return;
            }
        }
        if (!(plugin instanceof SpringSpigotPlugin)) {
            log.warn(plugin.getName() + " is not spring spigot plugin");
            return;
        }

        SpringSpigotPlugin springSpigotPlugin = (SpringSpigotPlugin) plugin;

        for (Class<?> needleBean : needleBeans) {
            String beanName = plugin.getName() + "." + needleBean.getCanonicalName();
            Object bean = springSpigotPlugin.getService(needleBean);

            registry.registerSingleton(beanName, bean);
        }
    }
}
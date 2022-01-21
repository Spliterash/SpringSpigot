package ru.spliterash.springspigot.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@Log
@RequiredArgsConstructor
public class AutoBeanConfig implements BeanDefinitionRegistryPostProcessor {
    /**
     * Путь сканирования пакетов
     */
    private final String packagePath;

    /**
     * Класс бина
     */
    private final Class<?> clazz;

    /**
     * Дополнительный фильтр если надо
     */
    private final Function<Class<?>, Boolean> additionalFilter;

    public AutoBeanConfig(String packagePath, Class<?> clazz) {
        this(packagePath, clazz, null);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Reflections reflections = new Reflections(packagePath);
        //noinspection unchecked
        Set<Class<?>> classes = (Set<Class<?>>) reflections.getSubTypesOf(clazz);

        Stream<Class<?>> stream = classes.stream();
        if (additionalFilter != null)
            stream = stream.filter(additionalFilter::apply);

        stream.filter(c -> !c.isInterface())
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .forEach(clazz -> {
                    try {
                        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                                .genericBeanDefinition(Class.forName(clazz.getName())).setLazyInit(false);
                        registry.registerBeanDefinition(clazz.getName(),
                                builder.getBeanDefinition());
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                    log.info("Creating - " + clazz.getSimpleName());
                });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}

package ru.spliterash.springspigot.reload;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DependencyGraphHelper {
    private final DefaultListableBeanFactory factory;

    public DependencyGraphHelper(DefaultListableBeanFactory factory) {
        this.factory = factory;
    }

    public <T> List<T> getSortedBeans(Class<T> type) {
        Map<String, T> beans = factory.getBeansOfType(type);

        Set<String> tree = buildTree(beans.keySet());

        List<T> result = new ArrayList<>(beans.size());

        for (String beanName : tree) {
            T bean = beans.get(beanName);
            if (bean != null)
                result.add(bean);
        }

        return result;

    }

    private Set<String> buildTree(Collection<String> beanNames) {
        LinkedHashSet<String> sorted = new LinkedHashSet<>();

        for (String bean : beanNames) {
            scan(sorted, bean);
        }

        return sorted;
    }

    private void scan(Set<String> sorted, String beanName) {
        String[] dependenciesForBean = factory.getDependenciesForBean(beanName);

        for (String dependencyBeanName : dependenciesForBean) {
            scan(sorted, dependencyBeanName);
        }

        sorted.add(beanName);
    }
}
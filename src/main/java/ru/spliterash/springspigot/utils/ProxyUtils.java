package ru.spliterash.springspigot.utils;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

@SuppressWarnings("unchecked")
public class ProxyUtils {
    public static <T> T getProxyTarget(Object proxy) {
        if (!AopUtils.isAopProxy(proxy))
            return (T) proxy;
        TargetSource targetSource = ((Advised) proxy).getTargetSource();
        return getTarget(targetSource);
    }

    private static <T> T getTarget(TargetSource targetSource) {
        try {
            return (T) targetSource.getTarget();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
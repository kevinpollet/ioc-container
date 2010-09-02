package com.my.container.context.beanfactory.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Helper class who provides useful
 * methods to deal with Java Proxy.
 *
 * @author kevinpollet
 */
public final class ProxyHelper {

    /**
     * This class cannot be instantiated.
     */
    private ProxyHelper() {
    }

    /**
     * This method return the proxied instance of the {@linkplain java.lang.reflect.Proxy Java Proxy}
     * in parameter.
     *
     * @param proxyInstance the instance of the proxy
     * @return the proxied instance or null
     * @throws IllegalArgumentException if proxy instance parameter is null, not a proxy
     *                                  or doesn't implements {@link com.my.container.context.beanfactory.proxy.AbstractBeanInvocationHandler}
     */
    public static <T> T getProxiedInstance(final T proxyInstance) {
        if (proxyInstance == null || !Proxy.isProxyClass(proxyInstance.getClass())) {
            throw new IllegalArgumentException("The proxy instance parameter cannot be null");
        }

        T instance = proxyInstance;

        InvocationHandler handler = Proxy.getInvocationHandler(proxyInstance);
        if (handler instanceof AbstractBeanInvocationHandler) {
            instance = (T) ((AbstractBeanInvocationHandler) handler).getProxiedInstance();
        } else {
            throw new IllegalArgumentException("This proxy instance doesn't implements AbstractBeanInvocationHandler");
        }

        return instance;
    }

}

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
     * @return the proxied instance or the parameter if :
     *         <li>it doesn't implements {@link com.my.container.context.beanfactory.proxy.AbstractBeanInvocationHandler}</li>
     *         <li>it's not a Java proxy</li>
     *         <p><b>This methods never returns null</b><p>
     * @throws IllegalArgumentException if proxy instance parameter is null
     */
    public static <T> T getTargetObject(final T proxyInstance) {
        if (proxyInstance == null) {
            throw new IllegalArgumentException("The proxy instance parameter cannot be null");
        }

        T instance = proxyInstance;

        if (Proxy.isProxyClass(proxyInstance.getClass())) {

            InvocationHandler handler = Proxy.getInvocationHandler(proxyInstance);
            if (handler instanceof AbstractBeanInvocationHandler) {
                instance = (T) ((AbstractBeanInvocationHandler) handler).getProxiedInstance();
            }
            
        }

        return instance;
    }

}

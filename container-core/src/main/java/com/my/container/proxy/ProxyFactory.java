package com.my.container.proxy;

import java.lang.reflect.Proxy;


public class ProxyFactory {

    /**
     * Create a proxy for an object
     * instance.
     *
     * @param instance the instance
     * @return the proxy
     */

    public static Object buildProxy(final Object instance) {

        return Proxy.newProxyInstance(instance.getClass().getClassLoader(),
                                            instance.getClass().getInterfaces(),
                                            new ProxyInvocationHandler(instance));
    }
}

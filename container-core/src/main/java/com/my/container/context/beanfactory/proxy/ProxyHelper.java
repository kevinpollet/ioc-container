/*
 * Copyright 2010 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
     * This method returns the proxied instance of the {@linkplain java.lang.reflect.Proxy Java Proxy}
     * in parameter.
     *
     * @param proxyInstance the instance of the proxy
     * @return The proxied instance or the parameter instance if :
     *         <ul><li>it doesn't implements {@link com.my.container.context.beanfactory.proxy.AbstractBeanAwareInvocationHandler}</li>
     *         <li>it's not a Java proxy</li></ul>
     * @throws IllegalArgumentException if proxy instance parameter is null
     */
    public static <T> T getTargetObject(final T proxyInstance) {
        if (proxyInstance == null) {
            throw new IllegalArgumentException("The proxy instance parameter cannot be null");
        }

        T instance = proxyInstance;

        if (Proxy.isProxyClass(proxyInstance.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(proxyInstance);
            if (handler instanceof AbstractBeanAwareInvocationHandler) {
                instance = (T) ((AbstractBeanAwareInvocationHandler) handler).getProxiedInstance();
            }
        }

        return instance;
    }

    /**
     * Get the target class of the object being proxied
     * by the proxy in parameter.
     *
     * @param proxyInstance the proxy instance
     * @return The proxied class or the parameter class instance if :
     *         <ul><li>it doesn't implements {@link AbstractBeanAwareInvocationHandler}</li>
     *         <li>it's not a Java proxy</li></ul>
     * @throws IllegalArgumentException if the proxy instance parameter is null
     */
    public static Class<?> getTargetClass(final Object proxyInstance) {
        if (proxyInstance == null) {
            throw new IllegalArgumentException("The proxy instance canot be null");
        }

        Object target = ProxyHelper.getTargetObject(proxyInstance);
        return target.getClass();
    }

}

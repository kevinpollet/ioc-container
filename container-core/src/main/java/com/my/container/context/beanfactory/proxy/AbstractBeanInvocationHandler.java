package com.my.container.context.beanfactory.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * A simple invocation handler who
 * contains the proxied instance
 * and permits to retrieve it.
 *
 * @author kevinpollet
 *         Date: 22 août 2010
 */
public abstract class AbstractBeanInvocationHandler implements InvocationHandler {

    private final Object proxiedInstance;

    /**
     * The AbstractBeanInvocationHandler constructor.
     *
     * @param proxiedInstance the instance to be proxied
     */
    public AbstractBeanInvocationHandler(final Object proxiedInstance) {
        this.proxiedInstance = proxiedInstance;
    }

    /**
     * Get the current proxied Instance.
     *
     * @return the proxied instance
     */
    public Object getProxiedInstance() {
        return this.proxiedInstance;
    }

}

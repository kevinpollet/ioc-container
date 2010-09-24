package com.my.container.context.beanfactory.handler;

import java.lang.reflect.InvocationHandler;

/**
 * A simple invocation handler who
 * contains the proxied instance
 * and permits to retrieve it.
 *
 * @author kevinpollet
 *         Date: 22 août 2010
 */
public abstract class AbstractBeanAwareInvocationHandler implements InvocationHandler {

    private final Object proxiedInstance;

    /**
     * The AbstractBeanInvocationHandler constructor.
     *
     * @param proxiedInstance the instance to be proxied
     */
    public AbstractBeanAwareInvocationHandler(final Object proxiedInstance) {
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

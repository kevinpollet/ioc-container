package com.my.container.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;


//Todo Populate interceptor
public class ProxyInvocationHandler implements InvocationHandler {

    private Object proxiedInstance;

    private List<Method> beforeMethodList;
    private List<Method> aroundMethodList;
    private List<Method> afterMethodList;

    /**
     * Create an invocation handler.
     * @param proxiedInstance the object tp proxy
     */
    public ProxyInvocationHandler(final Object proxiedInstance) {
        this.proxiedInstance = proxiedInstance;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxiedInstance, args);
    }

    /**
     * Get the proxied bean instance
     * @return the object being proxied
     */
    public Object getProxiedInstance() {
        return proxiedInstance;
    }
}

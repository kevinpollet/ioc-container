package com.my.container.context.beanfactory.proxy;

import com.my.container.annotations.interceptors.After;
import com.my.container.annotations.interceptors.Before;
import com.my.container.annotations.interceptors.ExcludeInterceptors;
import com.my.container.util.ReflectionHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * The interceptor invocation handler.
 *
 * @author kevinpollet
 */
public class InterceptorInvocationHandler implements InvocationHandler {

    private Object instance;
    private Object[] interceptors;

    /**
     * Create an invocation handler.
     *
     * @param instance     the object to proxy
     * @param interceptors the interceptor list
     */
    public InterceptorInvocationHandler(final Object instance, final Object[] interceptors) {
        this.instance = instance;
        this.interceptors = interceptors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //Call @Before interceptor method
        if (!method.isAnnotationPresent(ExcludeInterceptors.class)) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.callDeclaredMethodWith(Before.class, interceptor, instance, method, args);
            }
        }

        //Call the original method
        Object result = method.invoke(instance, args);

        //Call @After interceptor method
        if (!method.isAnnotationPresent(ExcludeInterceptors.class)) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.callDeclaredMethodWith(After.class, interceptor, instance, method, args);
            }
        }


        return result;
    }

    /**
     * Get the proxied bean instance.
     *
     * @return the object being proxied
     */
    public Object getProxiedInstance() {
        return this.instance;
    }

    /**
     * Get the interceptors.
     *
     * @return an array of the class interceptors
     */
    public Object[] getInterceptors() {
        return this.interceptors;
    }
}

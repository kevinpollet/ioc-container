package com.my.container.context.beanfactory.proxy;

import com.my.container.annotations.interceptors.After;
import com.my.container.annotations.interceptors.Before;
import com.my.container.annotations.interceptors.ExcludeInterceptors;
import com.my.container.util.ReflectionHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * The injectservice invocation handler.
 *
 * @author kevinpollet
 */
//TODO what happens if annotations are in the interface definition ???
public class InterceptorInvocationHandler implements InvocationHandler {

    private Object instance;
    private Object[] interceptors;

    /**
     * Create an invocation handler.
     *
     * @param instance     the object to proxy
     * @param interceptors the injectservice list
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

        //Retrieve the implementation annotation
        Method realMethod = instance.getClass().getMethod(method.getName(), method.getParameterTypes());
        boolean isExcludeInterceptor = realMethod.isAnnotationPresent(ExcludeInterceptors.class);

        //Call @Before injectservice method
        if (!isExcludeInterceptor) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.callDeclaredMethodWith(Before.class, interceptor, instance, realMethod, args);
            }
        }

        //Call the original method
        Object result = method.invoke(instance, args);

        //Call @After injectservice method
        if (!isExcludeInterceptor) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.callDeclaredMethodWith(After.class, interceptor, instance, realMethod, args);
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

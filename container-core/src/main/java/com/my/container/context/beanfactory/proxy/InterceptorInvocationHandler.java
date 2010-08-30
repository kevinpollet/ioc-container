package com.my.container.context.beanfactory.proxy;

import com.my.container.annotations.interceptors.After;
import com.my.container.annotations.interceptors.Before;
import com.my.container.annotations.interceptors.ExcludeInterceptors;
import com.my.container.util.ReflectionHelper;

import java.lang.reflect.Method;


/**
 * The interceptor invocation handler.
 *
 * @author kevinpollet
 */
//TODO what happens if annotations are in the interface definition ???
public class InterceptorInvocationHandler extends AbstractBeanInvocationHandler {

    private Object[] interceptors;

    /**
     * Create an interceptor invocation handler.
     *
     * @param instance     the object to proxy
     * @param interceptors the interceptor list
     */
    public InterceptorInvocationHandler(final Object instance, final Object[] interceptors) {
        super(instance);
        
        this.interceptors = interceptors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object realInstance = this.getProxiedInstance();
        Method realMethod = realInstance.getClass().getMethod(method.getName(), method.getParameterTypes());
        boolean isExcludeInterceptor = realMethod.isAnnotationPresent(ExcludeInterceptors.class);

        if (!isExcludeInterceptor) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.invokeDeclaredMethodWith(Before.class, interceptor, realInstance, realMethod, args);
            }
        }

        Object result = super.invoke(proxy, method, args);

        if (!isExcludeInterceptor) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.invokeDeclaredMethodWith(After.class, interceptor, realInstance, realMethod, args);
            }
        }

        return result;
    }
    
}

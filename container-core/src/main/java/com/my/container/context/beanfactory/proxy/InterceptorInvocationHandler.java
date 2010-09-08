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
//TODO remove interception of toString, hashcode and equals methods
public class InterceptorInvocationHandler extends AbstractBeanAwareInvocationHandler {

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

        Method realMethod = this.getProxiedInstance().getClass().getMethod(method.getName(), method.getParameterTypes());
        
        boolean isObjectMethod = method.getDeclaringClass().equals(Object.class);
        boolean isExcludeInterceptor = realMethod.isAnnotationPresent(ExcludeInterceptors.class);

        if (!isObjectMethod && !isExcludeInterceptor) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.invokeDeclaredMethodWith(Before.class, interceptor, this.getProxiedInstance(), realMethod, args);
            }
        }

        Object result = method.invoke(this.getProxiedInstance(), args);

        if (!isObjectMethod && !isExcludeInterceptor) {
            for (Object interceptor : interceptors) {
                ReflectionHelper.invokeDeclaredMethodWith(After.class, interceptor, this.getProxiedInstance(), realMethod, args);
            }
        }

        return result;
    }
    
}

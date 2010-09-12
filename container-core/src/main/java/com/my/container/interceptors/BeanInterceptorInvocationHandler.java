package com.my.container.interceptors;

import com.my.container.context.beanfactory.proxy.AbstractBeanAwareInvocationHandler;
import com.my.container.interceptors.annotations.After;
import com.my.container.interceptors.annotations.Before;
import com.my.container.interceptors.annotations.ExcludeInterceptors;
import com.my.container.util.ReflectionHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * The interceptor invocation handler.
 *
 * @author kevinpollet
 */
public class BeanInterceptorInvocationHandler extends AbstractBeanAwareInvocationHandler {

    private final Method aroundMethod;

    private final Object[] interceptors;

    /**
     * Create an interceptor invocation handler.
     *
     * @param instance     the object to proxy
     * @param interceptors the interceptor list
     */
    public BeanInterceptorInvocationHandler(final Object instance, final Object[] interceptors, final Method aroundMethod) {
        super(instance);
        this.interceptors = interceptors;
        this.aroundMethod = aroundMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Method realMethod = this.getProxiedInstance().getClass().getMethod(method.getName(), method.getParameterTypes());
        boolean isExcludeInterceptor = realMethod.isAnnotationPresent(ExcludeInterceptors.class);
        boolean isObjectMethod = method.getDeclaringClass().equals(Object.class);


        if (isObjectMethod || isExcludeInterceptor) {
           result = method.invoke(this.getProxiedInstance(), args);
            
        } else {
            // Call before interceptor methods
            if (interceptors != null) {
                for (Object interceptor : interceptors) {
                    ReflectionHelper.invokeDeclaredMethodWith(Before.class, interceptor, new JoinPoint(this.getProxiedInstance(), method, args));
                }
            }

            if (aroundMethod != null) {
                if (Modifier.isPrivate(aroundMethod.getModifiers())) {
                    aroundMethod.setAccessible(true);
                }
                result = aroundMethod.invoke(this.getProxiedInstance(), new ProceedingJoinPoint(this.getProxiedInstance(), method, args));
            } else {
                result = method.invoke(this.getProxiedInstance(), args);
            }
            
            // Call after interceptor method
            if (interceptors != null) {
                for (Object interceptor : interceptors) {
                    ReflectionHelper.invokeDeclaredMethodWith(After.class, interceptor, new JoinPoint(this.getProxiedInstance(), method, args));
                }
            }
        }
        

        return result;
    }
    
    
}

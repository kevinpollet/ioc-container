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
package com.my.container.aop;

import com.my.container.aop.invocation.JoinPoint;
import com.my.container.aop.invocation.ProceedingJoinPoint;
import com.my.container.core.beanfactory.proxy.AbstractBeanAwareInvocationHandler;
import com.my.container.aop.annotations.After;
import com.my.container.aop.annotations.Before;
import com.my.container.aop.annotations.ExcludeInterceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * The interceptor invocation handler.
 *
 * @author kevinpollet
 */
public class InterceptorInvocationHandler extends AbstractBeanAwareInvocationHandler {

    private final Object[] interceptors;

    private final Method aroundMethod;

    /**
     * Create an interceptor invocation handler.
     *
     * @param instance     the object to proxy
     * @param interceptors the interceptor list
     * @param aroundMethod the aroundMethod
     */
    public InterceptorInvocationHandler(final Object instance, final Object[] interceptors, final Method aroundMethod) {
        super(instance);

        this.interceptors = interceptors;
        this.aroundMethod = aroundMethod;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(final Object proxy,final Method method,final Object[] parameters) throws Throwable {
        Object result = null;
        Method realMethod = this.getProxiedInstance().getClass().getMethod(method.getName(), method.getParameterTypes());

        boolean isExcludeInterceptor = realMethod.isAnnotationPresent(ExcludeInterceptors.class);
        boolean isObjectMethod = method.getDeclaringClass().equals(Object.class);

        //Before
        if (interceptors != null && !isObjectMethod && !isExcludeInterceptor) {
            this.callInterceptorMethod(Before.class, new JoinPoint(this.getProxiedInstance(), realMethod, parameters));
        }


        if (aroundMethod != null) {

            if (Modifier.isPrivate(aroundMethod.getModifiers())) {
                aroundMethod.setAccessible(true);
            }

            result = aroundMethod.invoke(this.getProxiedInstance(), new ProceedingJoinPoint(this.getProxiedInstance(), method, parameters));

        } else {
            
            result = method.invoke(this.getProxiedInstance(), parameters);
        }


        // After
        if (interceptors != null && !isObjectMethod && !isExcludeInterceptor) {
            this.callInterceptorMethod(After.class, new JoinPoint(this.getProxiedInstance(), realMethod, parameters));
        }

        return result;
    }


    /**
     * This method permits to call a public, private
     * protected interceptor method annotated with
     * the annotation in parameter.
     *
     * @param annotation the annotation class
     * @param jp the join point
     * @throws Exception the exception throws by the method
     */
    private void callInterceptorMethod(final Class<? extends Annotation> annotation, final JoinPoint jp) throws Exception {

        for (Object interceptor : interceptors) {
            Method[] methods = interceptor.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(annotation)) {
                    if (Modifier.isPrivate(m.getModifiers())) {
                        m.setAccessible(true);
                    }
                    
                    m.invoke(interceptor, jp);
                    break;
                }
            }
        }
        
    }


}

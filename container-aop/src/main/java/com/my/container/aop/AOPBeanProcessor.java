/*
 * Copyright 2011 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.aop;

import com.my.container.aop.annotations.AroundInvoke;
import com.my.container.aop.annotations.Interceptors;
import com.my.container.engine.spi.BeanProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * The AOP invocation processor. 
 *
 * @author kevinpollet
 */
public class AOPBeanProcessor implements BeanProcessor {

    /**
     * {@inheritDoc}
     */
    public boolean isProcessable(final Object bean) {
        Class<?> beanClass = bean.getClass();
        return beanClass.isAnnotationPresent(Interceptors.class) || this.isAroundMethod(beanClass);
    }

    /**
     * {@inheritDoc}
     */
    public <T> T processBean(final T bean) throws Exception {
        Class<?> beanClass = bean.getClass();

        List<Object> interceptors = new ArrayList<Object>();
        Method aroundMethod = null;

        // Get around method
        Method[] methods = beanClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(AroundInvoke.class)) {
                aroundMethod = m;
                break;
            }
        }

        // Create interceptor
        if (beanClass.isAnnotationPresent(Interceptors.class)) {
            Interceptors annotation = beanClass.getAnnotation(Interceptors.class);
            Class<?>[] interceptorsClass = annotation.value();
            for (Class<?> intClass : interceptorsClass) {
                try {

                    interceptors.add(intClass.newInstance());

                } catch (InstantiationException e) {
                    //
                } catch (IllegalAccessException e) {
                    //
                }
            }
            
        }

        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                                          beanClass.getInterfaces(),
                                          new InterceptorInvocationHandler(bean, interceptors.toArray(), aroundMethod));
    }


    /**
     * Check if the class in parameter have an
     * @AroundInvoke method.
     *
     * @param clazz the class
     * @return true if the class contains a method annotated with @AroundInvoke, false otherwise
     *
     */
    private boolean isAroundMethod(final Class<?> clazz) {
        boolean result = false;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(AroundInvoke.class)) {
                result = true;
                break;
            }
        }

        return result;
    }


}

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
package com.my.container.context.beanfactory.injector;

import com.my.container.binding.Binding;
import com.my.container.context.beanfactory.exceptions.BeanDependencyInjectionException;
import com.my.container.context.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.context.beanfactory.proxy.ProxyHelper;
import com.my.container.util.ReflectionHelper;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Definition pf an Injector of fields.
 *
 * @author Kevin Pollet
 */
public class MethodInjector {

    /**
     * The Logger.
     */
    private final Injector injector;

    /**
     * Construct a Method injector.
     *
     * @param injector the parent injector
     */
    public MethodInjector(final Injector injector) {
        this.injector = injector;
    }

    /**
     * Inject method parameters of methods
     * annotated by @Inject annotation.
     *
     * @param context  the injection context
     * @param clazz    the current class injected
     * @param instance the instance injected
     */
    public void injectMethodsDependencies(final InjectionContext context, final Class<?> clazz, final Object instance) {

        //Inject SuperClass first
        if (clazz.getSuperclass() != null) {
            this.injectMethodsDependencies(context, clazz.getSuperclass(), instance);
        }

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Inject.class)) {

                boolean isOverridden = ReflectionHelper.isOverridden(ProxyHelper.getTargetClass(instance), method);
                boolean isAccessible = !Modifier.isPrivate(method.getModifiers());

                if (Modifier.isAbstract(method.getModifiers())) {
                    throw new BeanDependencyInjectionException(String.format("Injection of dependencies in abstract method %s is invalid in class %s.", method.getName(), clazz.getName()));
                }

                if (!isOverridden) {

                    Class<?>[] parametersClass = method.getParameterTypes();
                    Object[] parameters = new Object[parametersClass.length];
                    Annotation[][] parametersAnnotation = method.getParameterAnnotations();

                    //Mark this class
                    context.getCyclicHandlerMap().put(instance.getClass(), instance);

                    for (int i = 0; i < parametersClass.length; i++) {

                        Binding<?> binding;
                        Annotation qualifier = null;

                        if (parametersClass[i].equals(clazz)) {
                            throw new BeanDependencyInjectionException(String.format("Method injection for %s cannot have a parameter of their own type.", parametersClass[i].getSimpleName()));
                        }

                        //Get parameter qualifier
                        for (Annotation annotation : parametersAnnotation[i]) {
                            if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                                qualifier = annotation;
                                break;
                            }
                        }

                        //Get binding
                        if (qualifier != null) {
                            binding = context.getBeanFactory().getBindingHolder().getQualifiedBindingFor(parametersClass[i], qualifier);
                        } else {
                            binding = context.getBeanFactory().getBindingHolder().getBindingFor(parametersClass[i]);
                        }

                        if (binding == null) {
                            throw new NoSuchBeanDefinitionException(String.format("There is no binding defined for the class %s", parametersClass[i].getName()));
                        }

                        //Create and store parameter
                        parameters[i] = this.injector.constructClass(context, binding.getImplementation());

                    }


                    //Call method
                    try {

                        if (!isAccessible) {
                            method.setAccessible(true);
                        }

                        method.invoke(ProxyHelper.getTargetObject(instance), parameters);

                        //Remove mark
                        context.getCyclicHandlerMap().remove(instance.getClass());

                    } catch (IllegalAccessException ex) {
                        throw new BeanDependencyInjectionException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new BeanDependencyInjectionException(ex);
                    }


                }

            }

        }


    }

}

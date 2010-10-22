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
import com.my.container.util.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Definition an Injector of fields.
 * 
 * @author Kevin Pollet
 */
public class FieldInjector {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(FieldInjector.class);

    /**
     * The parent injector.
     */
    private final Injector injector;

    /**
     * Create a fields injector
     *
     * @param injector the parent injector
     */
    public FieldInjector(final Injector injector) {
        this.injector = injector;
    }

    /**
     * Inject dependency field annotated by @Inject.
     *
     * @param context  the injection context
     * @param clazz    the current class injected
     * @param instance the bean instance injected
     */
    public void injectFieldsDependencies(final InjectionContext context, final Class<?> clazz, final Object instance) {

        //Inject SuperClass first
        if (clazz.getSuperclass() != null) {
            this.injectFieldsDependencies(context, clazz.getSuperclass(), instance);
        }

        Field[] fieldsOfClass = clazz.getDeclaredFields();
        for (Field field : fieldsOfClass) {

            if (field.isAnnotationPresent(Inject.class)) {

                Binding<?> binding;
                Annotation qualifier = null;
                boolean isAccessible = !Modifier.isPrivate(field.getModifiers());

                if (Modifier.isFinal(field.getModifiers())) {
                    throw new BeanDependencyInjectionException(String.format("Cannot inject final field %s in class %s", field.getName(), field.getDeclaringClass().getName()));
                }

                //Class have dependencies mark it
                context.getCyclicHandlerMap().put(instance.getClass(), instance);

                //Get qualifier
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                        qualifier = annotation;
                        break;
                    }
                }

                //Retrieve binding
                if (qualifier == null) {
                    binding = context.getBeanFactory().getBindingHolder().getBindingFor(field.getType());
                } else {
                    binding = context.getBeanFactory().getBindingHolder().getBindingFor(field.getType(), qualifier);
                }

                if (binding == null) {
                    throw new NoSuchBeanDefinitionException(String.format("There is no binding defined for the class %s", field.getType().getName()));
                }

                //Create and set field instance
                try {

                    if (!isAccessible) {
                        field.setAccessible(true);
                    }

                    field.set(ProxyHelper.getTargetObject(instance), this.injector.constructClass(context, binding.getImplementation()));

                    //Injection is done remove mark
                    context.getCyclicHandlerMap().remove(instance.getClass());

                } catch (IllegalAccessException ex) {
                    throw new BeanDependencyInjectionException(String.format("The field %s is not accessible and cannot be injected", field.getName()), ex);
                }

            }

        }

    }

}

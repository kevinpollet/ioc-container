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
import com.my.container.context.beanfactory.BeanFactory;
import com.my.container.context.beanfactory.exceptions.BeanDependencyInjectionException;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.context.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.spi.BeanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Definition of an injector.
 *
 * @author Kevin Pollet
 */
public class Injector {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(Injector.class);

    /**
     * The field injector.
     */
    private final FieldInjector fieldInjector;

    /**
     * The method injector.
     */
    private final MethodInjector methodInjector;

    /**
     * Construct an injector.
     */
    public Injector() {
        this.fieldInjector = new FieldInjector(this);
        this.methodInjector = new MethodInjector(this);
    }

    /**
     * Construct an instance of the given class and resolve
     * dependencies of the newly created instance.
     *
     * @param context the injection context
     * @param clazz   the class
     * @param <T>     the type of the class
     * @return an instance of the class with it's properties resolved
     */
    public <T> T constructClass(final InjectionContext context, final Class<T> clazz) {

        T createdBean = null;
        BeanFactory factory = context.getBeanFactory();

        if (factory.getSingletonBeans().containsKey(clazz)) {
            return clazz.cast(context.getBeanFactory().getSingletonBeans().get(clazz));
        }

        if (context.getCyclicHandlerMap().containsKey(clazz)) {
            Object objectRef = context.getCyclicHandlerMap().get(clazz);
            if (objectRef == null) {
                throw new BeanDependencyInjectionException("Cyclic dependencies in constructor are not valid.");
            }

            return clazz.cast(objectRef);
        }

        try {

            Constructor[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {

                if (constructor.isAnnotationPresent(Inject.class)) {

                    Class<?>[] parametersClass = constructor.getParameterTypes();
                    Object[] parameters = new Object[parametersClass.length];
                    Annotation[][] parametersAnnotations = constructor.getParameterAnnotations();

                    if (parametersClass.length != 0) {

                        context.getCyclicHandlerMap().put(clazz, null);

                        for (int i = 0; i < parametersClass.length; i++) {

                            Binding binding;
                            Annotation qualifier = null;

                            //Get qualifier
                            for (Annotation annotation : parametersAnnotations[i]) {
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

                            parameters[i] = this.constructClass(context, binding.getImplementation());
                        }

                        createdBean = clazz.cast(constructor.newInstance(parameters));
                        context.getCyclicHandlerMap().remove(clazz);
                    }

                    break;
                }

            }

            if (createdBean == null) {
                createdBean = clazz.newInstance();
            }

            //Apply bean post processor
            List<BeanProcessor> beanProcessors = context.getBeanFactory().getBeanProcessors();
            try {

                for (BeanProcessor processor : beanProcessors) {
                    if (processor.isProcessable(createdBean)) {
                        createdBean = processor.processBean(createdBean);
                    }
                }

            } catch (Exception ex) {
                throw new BeanInstantiationException("Error when applying bean post processors", ex);
            }

            //Hold singletons and prototypes bean
            if (clazz.isAnnotationPresent(Singleton.class)) {
                context.getBeanFactory().getSingletonBeans().put(clazz, createdBean);
            } else {
                context.getBeanFactory().getPrototypeBeans().add(createdBean);
            }

            //Inject dependencies in this newly created bean
            this.injectExistingInstance(context, createdBean);

            //Hold created bean
            context.getNewlyCreatedBeans().add(createdBean);

        } catch (Exception ex) {
            throw new BeanInstantiationException(String.format("The %s class cannot be instantiated", clazz.getName()), ex);
        }

        return createdBean;
    }

    /**
     * Inject dependencies into an existing beans.
     *
     * @param context the injection context
     * @param bean    the bean to be injected
     */
    public void injectExistingInstance(final InjectionContext context, final Object bean) {
        this.fieldInjector.injectFieldsDependencies(context, bean.getClass(), bean);
        this.methodInjector.injectMethodsDependencies(context, bean.getClass(), bean);
    }

}

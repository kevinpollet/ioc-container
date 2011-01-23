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
package com.my.container.core.beanfactory;

import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.binding.MapBindingHolder;
import com.my.container.binding.ProvidedBinding;
import com.my.container.core.beanfactory.exceptions.CallbackInvocationException;
import com.my.container.core.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.core.beanfactory.injector.InjectionContext;
import com.my.container.core.beanfactory.injector.Injector;
import com.my.container.core.beanfactory.spi.BeanProcessor;
import com.my.container.util.loader.ServiceLoader;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO callbacks method verification
//TODO asynchronous bean destruction can be good if one PreDestroy crash :p

/**
 * The factory of bean class
 *
 * @author kevinpollet
 */
public final class BeanFactory {

    private final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final BindingHolder holder;

    private final BindingHolder providerHolder;

    private final List<Object> prototypesBean;

    private final Map<Class<?>, Object> singletonsBean;

    private final List<BeanProcessor> beanProcessors;

    private final Injector injector;

    /**
     * Bean Factory constructor.
     *
     * @param list the binding list
     */
    public BeanFactory(final List<Binding<?>> list) {
        this.holder = new MapBindingHolder();
        this.providerHolder = new MapBindingHolder();

        this.prototypesBean = new ArrayList<Object>();
        this.singletonsBean = new HashMap<Class<?>, Object>();
        this.beanProcessors = new ArrayList<BeanProcessor>();

        this.injector = new Injector();

        //Load Bean processor
        ServiceLoader<BeanProcessor> loader = ServiceLoader.load(BeanProcessor.class);
        for (BeanProcessor processor : loader) {
            this.beanProcessors.add(processor);
        }

        //Populate holder
        if (list != null) {
            for (Binding b : list) {
                if (b instanceof ProvidedBinding) {
                    this.providerHolder.put(b);
                } else {
                    this.holder.put(b);
                }
            }
        }
    }

    /**
     * Get a bean defined by this class.
     * This class mut be the interface of the binding.
     *
     * @param clazz the bean class
     * @return the bean instance
     * @throws NoSuchBeanDefinitionException if there is no binding implementation for this bean contract
     * @throws IllegalArgumentException      if the clazz argument is null
     */
    public <T> T getBean(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        } else if (!this.holder.isBindingFor(clazz)) {
            throw new NoSuchBeanDefinitionException(String.format("The class %s have no binding defined", clazz.getSimpleName()));
        }

        T createdBean;
        Binding<T> binding = this.holder.getBindingFor(clazz);
        Class<? extends T> toClass = binding.getImplementation();

        if (binding.getImplementation().isAnnotationPresent(Singleton.class) && this.singletonsBean.containsKey(clazz)) {
            return clazz.cast(this.singletonsBean.get(toClass));
        }

        InjectionContext context = new InjectionContext(this);
        createdBean = injector.constructClass(context, toClass);

        //Call PostConstruct method on newly created bean
        ReflectionHelper.invokePostConstructCallback(context.getNewlyCreatedBeans().toArray());

        return createdBean;
    }

    /**
     * This method permits to resolve dependencies of an
     * existing bean. Generally this bean was created with
     * the Java new operator.
     *
     * @param bean the existing bean
     */
    public void resolveDependencies(final Object bean) {

        InjectionContext context = new InjectionContext(this);
        this.injector.injectDependencies(context, bean);

        //Call PostConstruct method on newly created bean
        ReflectionHelper.invokePostConstructCallback(context.getNewlyCreatedBeans().toArray());
    }

    /**
     * This method remove all beans references
     * memorised by the singleton for PreDestroy
     * CallBack and Singleton scope.
     *
     * @throws CallbackInvocationException if the invocation of a PreDestroy method failed
     */
    public void removeAllBeansReferences() {
        //Create list for all beans (prototype and singletons)
        List<Object> allBeans = new ArrayList<Object>() {{
            addAll(prototypesBean);
            addAll(singletonsBean.values());
        }};

        try {

            ReflectionHelper.invokePreDestroyCallback(allBeans.toArray());

        } finally {
            allBeans.clear();
            this.prototypesBean.clear();
            this.singletonsBean.clear();
        }
    }

    /**
     * Get the singleton beans map.
     *
     * @return the singleton map
     */
    public Map<Class<?>, Object> getSingletonBeans() {
        return this.singletonsBean;
    }

    /**
     * Get the list of prototypes bean created.
     *
     * @return the list of prototype bean
     */
    public List<Object> getPrototypeBeans() {
        return this.prototypesBean;
    }

    /**
     * Get the bean processors list.
     *
     * @return the bean processors list
     */
    public List<BeanProcessor> getBeanProcessors() {
        return this.beanProcessors;
    }

    /**
     * Get the binding holder.
     *
     * @return the binding holder
     */
    public BindingHolder getBindingHolder() {
        return this.holder;
    }

    /**
     * Get the provided binding holder.
     *
     * @return the provided binding holder
     */
    public BindingHolder getProviderHolder() {
        return this.providerHolder;
    }

}

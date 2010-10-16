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
package com.my.container.context;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.beanfactory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO class interface abstract ??

/**
 * The basic implementation of the
 * context interface.

 * @author kevinpollet
 */
public class ApplicationContext implements Context {

    private final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private BeanFactory factory;

    /**
     * Construct the context.
     *
     * @param provider the provider of bindings
     */
    public ApplicationContext(final BindingProvider provider) {
        provider.configureBindings();
        this.factory = new BeanFactory(provider.getBindings());
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getBean(final Class<T> clazz) {
        return this.factory.getBean(clazz);
    }

    /**
     * {@inheritDoc}
     */
    public void resolveBeanDependencies(final Object bean) {
        this.factory.resolveDependencies(bean);
    }

    /**
     * {@inheritDoc}
     */
    public void registerShutdownHook() {
        Thread thread = new Thread(new CallbackShutdownHook(this.factory));
        Runtime.getRuntime().addShutdownHook(thread);
    }
}

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

import com.my.container.context.beanfactory.BeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define the injection context.
 * @author Kevin Pollet
 */
public class InjectionContext {

    private final BeanFactory beanFactory;

    private final List<Object> newlyCreatedBeans;

    /**
     * A map to handle cyclic dependencies
     * during injection.
     */
    private Map<Class<?>, Object> cyclicHandlerMap;

    /**
     * Construct an InjectionContext.
     *
     * @param beanFactory the bean factory.
     */
    public InjectionContext(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.newlyCreatedBeans = new ArrayList<Object>();
        this.cyclicHandlerMap = new HashMap<Class<?>, Object>();
    }

    /**
     * Get the bean factory.
     *
     * @return the bean factory
     */
    public BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    /**
     * Get the cyclic handle map.
     *
     * @return the map
     */
    public Map<Class<?>, Object> getCyclicHandlerMap() {
        return this.cyclicHandlerMap;
    }

    /**
     * Get the newly created bean.
     *
     * @return the newly created bean
     */
    public List<Object> getNewlyCreatedBeans() { 
        return this.newlyCreatedBeans; 
    }

}

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
package com.my.container.engine;

import java.util.HashMap;
import java.util.Map;

import com.my.container.BeanStore;
import com.my.container.InjectionContext;

/**
 * @author Kevin Pollet
 */
public class InjectionContextImpl implements InjectionContext {

	private final boolean staticInjection;

    private final BeanStore beanFactory;

    private final Map<Class<?>, Object> processedBean;

    /**
     * Construct an InjectionContext.
     *
     * @param beanFactory the bean factory.
     */
    public InjectionContextImpl(BeanStore beanFactory, boolean staticInjection) {
        this.beanFactory = beanFactory;
		this.staticInjection = staticInjection;
        this.processedBean = new HashMap<Class<?>, Object>();
    }

	public boolean isStaticInjection() {
		return staticInjection;
	}

	public BeanStore getBeanStore() {
		return beanFactory;
	}

	public void markClassAsProcessed(Class<?> clazz, Object result) {
		processedBean.put( clazz, result );
	}

	public void removeMarkFor(Class<?> clazz) {
		processedBean.remove( clazz );
	}

	public boolean isAlreadyProcessed(Class<?> clazz) {
		return processedBean.containsKey( clazz );
	}

	public <T> T getResultOfProcessionFor(Class<T> clazz) {
		return clazz.cast( processedBean.get( clazz ) );
	}
}

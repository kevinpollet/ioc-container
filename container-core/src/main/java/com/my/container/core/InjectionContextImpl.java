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
package com.my.container.core;

import java.util.HashMap;
import java.util.Map;

import com.my.container.ContextBeanFactory;
import com.my.container.InjectionContext;

/**
 * @author Kevin Pollet
 */
public class InjectionContextImpl implements InjectionContext {

    private final ContextBeanFactory beanFactory;

    private final Map<Class<?>, Object> alreadyInjectedBean;

    /**
     * Construct an InjectionContext.
     *
     * @param beanFactory the bean factory.
     */
    public InjectionContextImpl(ContextBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.alreadyInjectedBean = new HashMap<Class<?>, Object>();
    }

	public ContextBeanFactory getContextBeanFactory() {
		return beanFactory;
	}

	public Map<Class<?>, Object> getAlreadyInjectedBean() {
		return alreadyInjectedBean;
	}

}

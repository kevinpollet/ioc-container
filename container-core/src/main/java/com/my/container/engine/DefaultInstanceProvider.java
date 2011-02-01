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

import javax.inject.Provider;

import com.my.container.BeanStore;

/**
 * The default provider. This provider is used
 * when a Provider is injected and the user provides
 * no custom provider.
 *
 * @author Kevin Pollet
 */
public class DefaultInstanceProvider<T> implements Provider<T> {

	private final BeanStore beanStore;

	private final Class<T> classToProvide;

	/**
	 * Construct an instance of default provider.
	 *
	 * @param beanStore the beanStore to get an instance
	 * @param classToProvide the bean class to create on each {@link javax.inject.Provider#get()} method call
	 */
	public DefaultInstanceProvider(BeanStore beanStore, Class<T> classToProvide) {
		this.beanStore = beanStore;
		this.classToProvide = classToProvide;
	}

	/**
	 * {@inheritDoc}
	 */
	public T get() {
		InjectionContextImpl context = new InjectionContextImpl( beanStore, false );
		return beanStore.getInjector().constructClass( context, classToProvide );
	}

}

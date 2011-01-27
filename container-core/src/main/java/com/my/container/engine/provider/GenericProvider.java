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
package com.my.container.engine.provider;

import javax.inject.Provider;

import com.my.container.ContextBeanStore;
import com.my.container.engine.InjectionContextImpl;
import com.my.container.engine.injector.ConstructorInjector;

/**
 * The default provider. This provider is used
 * when a Provider is injected and the user provides
 * no custom provider.
 *
 * @author Kevin Pollet
 */
public class GenericProvider<T> implements Provider<T> {

	private final ContextBeanStore contextBeanFactory;

	private final Class<T> classToProvide;

	/**
	 * Construct an instance of default provider.
	 *
	 * @param factory the contextBeanFactory to create the injected instance
	 * @param classToProvide the bean class to create on each {@link javax.inject.Provider#get()} method call
	 */
	public GenericProvider(ContextBeanStore factory, Class<T> classToProvide) {
		this.contextBeanFactory = factory;
		this.classToProvide = classToProvide;
	}

	/**
	 * {@inheritDoc}
	 */
	public T get() {
		InjectionContextImpl context = new InjectionContextImpl( contextBeanFactory, false );
		return new ConstructorInjector().constructClass( context, classToProvide );
	}

}

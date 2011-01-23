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
package com.my.container.core.beanfactory.injector;

import com.my.container.core.beanfactory.BeanFactory;
import com.my.container.util.ReflectionHelper;

import javax.inject.Provider;

/**
 * The default provider. This provider is used
 * when a Provider is injected and the user provides
 * no custom provider.
 *
 * @author Kevin Pollet
 */
public class DefaultInstanceProvider<T> implements Provider<T> {

	private final BeanFactory factory;

	private final Injector injector;

	private final Class<T> classToProvide;

	/**
	 * Construct an instance of default provider.
	 *
	 * @param factory the factory to create the injected instance
	 * @param classToProvide the bean class to create on each {@link javax.inject.Provider#get()} method call
	 */
	public DefaultInstanceProvider(final BeanFactory factory, final Class<T> classToProvide) {
		this.injector = new Injector();
		this.factory = factory;
		this.classToProvide = classToProvide;
	}

	/**
	 * {@inheritDoc}
	 */
	public T get() {

		InjectionContext context = new InjectionContext( factory );
		T instance = injector.constructClass( context, classToProvide );

		ReflectionHelper.invokePostConstructCallback( context.getNewlyCreatedBeans().toArray() );

		return instance;
	}

}

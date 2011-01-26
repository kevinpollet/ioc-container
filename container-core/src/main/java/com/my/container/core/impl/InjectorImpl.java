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
package com.my.container.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.my.container.binding.Binding;
import com.my.container.binding.BindingProvider;
import com.my.container.core.Configuration;
import com.my.container.core.Injector;
import com.my.container.core.beanfactory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * The basic implementation of the
 * core interface.
 *
 * @author Kevin Pollet
 */
class InjectorImpl extends Injector {

	private final Logger logger = LoggerFactory.getLogger( InjectorImpl.class );

	private BeanFactory factory;

	/**
	 * Construct the core.
	 *
	 * @param configuration the configuration object
	 */
	//TODO add all bindings is it right ?
	public InjectorImpl(Configuration configuration) {
		InjectorConfiguration config = (InjectorConfiguration) configuration;

		List<Binding<?>> bindings = new ArrayList<Binding<?>>();
		for ( BindingProvider provider : config.getBindingProviders() ) {
			provider.configureBindings();
			bindings.addAll( provider.getBindings() );
		}

		this.factory = new BeanFactory( bindings );
		if ( config.isShutDownHookEnable() ) {
			registerShutdownHook();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T getBean(final Class<T> clazz) {
		return this.factory.getBean( clazz );
	}

	/**
	 * {@inheritDoc}
	 */
	public void injectStatics(Class<?> clazz) {
		throw new NotImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void injectDependencies(final Object bean) {
		this.factory.resolveDependencies( bean );
	}

	/**
	 * {@inheritDoc}
	 */
	private void registerShutdownHook() {
		Thread thread = new Thread( this.new CallbackShutdownHook( this.factory ) );
		Runtime.getRuntime().addShutdownHook( thread );
	}


	/**
	 * Shutdown hook class.
	 */
	private class CallbackShutdownHook implements Runnable {

		private final Logger logger = LoggerFactory.getLogger( CallbackShutdownHook.class );

		private final BeanFactory factory;

		/**
		 * The CallbackShutdown hook factory.
		 *
		 * @param factory the bean factory
		 */
		public CallbackShutdownHook(BeanFactory factory) {
			this.factory = factory;
		}

		/**
		 * {@inheritDoc}
		 */
		public void run() {
			logger.info( "Shutdown hook called : Call all created bean PreDestroy methods" );
			this.factory.removeAllBeansReferences();
		}
	}

}

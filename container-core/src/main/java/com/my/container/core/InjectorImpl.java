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

import java.util.ArrayList;
import java.util.List;

import com.my.container.ContextBeanFactory;
import com.my.container.binding.Binding;
import com.my.container.binding.BindingProvider;
import com.my.container.Configuration;
import com.my.container.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * The basic implementation of the core interface.
 *
 * @author Kevin Pollet
 */
class InjectorImpl extends Injector {

	private final ContextBeanFactory context;

	/**
	 * Construct the core.
	 *
	 * @param configuration the configuration object
	 */
	public InjectorImpl(Configuration configuration) {
		ConfigurationImpl config = (ConfigurationImpl) configuration;

		List<Binding<?>> bindings = new ArrayList<Binding<?>>();
		for ( BindingProvider provider : config.getBindingProviders() ) {
			provider.configureBindings();
			bindings.addAll( provider.getBindings() );
		}

		this.context = new ContextBeanFactoryImpl( bindings );
		if ( config.isShutDownHookEnable() ) {
			registerShutdownHook();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T get(Class<T> clazz) {
		return context.constructBean( clazz );
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
	public void injectDependencies(Object bean) {
		context.injectExistingBean( bean );
	}

	/**
	 * {@inheritDoc}
	 */
	private void registerShutdownHook() {
		Thread thread = new Thread( this.new CallbackShutdownHook( this.context ) );
		Runtime.getRuntime().addShutdownHook( thread );
	}


	/**
	 * Shutdown hook inner class.
	 */
	private class CallbackShutdownHook implements Runnable {

		private final Logger logger = LoggerFactory.getLogger( CallbackShutdownHook.class );

		private final ContextBeanFactory context;

		/**
		 * The CallbackShutdown hook context.
		 *
		 * @param context the bean context
		 */
		public CallbackShutdownHook(ContextBeanFactory context) {
			this.context = context;
		}

		/**
		 * {@inheritDoc}
		 */
		public void run() {
			logger.info( "Shutdown hook called : Call all created bean PreDestroy methods" );
			context.destroy();
		}
	}

}

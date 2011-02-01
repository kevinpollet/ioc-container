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

import java.util.ArrayList;
import java.util.List;

import com.my.container.BeanStore;
import com.my.container.Configuration;
import com.my.container.Container;
import com.my.container.InjectionContext;
import com.my.container.binding.Binding;
import com.my.container.binding.BindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The basic implementation of the container interface.
 *
 * @author Kevin Pollet
 */
public class ContainerImpl extends Container {

	private final BeanStore beanStore;

	/**
	 * Construct the engine.
	 *
	 * @param configuration the configuration object
	 */
	public ContainerImpl(Configuration configuration) {
		ContainerConfigurationImpl config = (ContainerConfigurationImpl) configuration;

		List<Binding<?>> bindings = new ArrayList<Binding<?>>();
		for ( BindingProvider provider : config.getBindingProviders() ) {
			provider.configureBindings();
			bindings.addAll( provider.getBindings() );
		}

		this.beanStore = new BeanStoreImpl( bindings );
		if ( config.isShutDownHookEnable() ) {
			registerShutdownHook();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T get(Class<T> clazz) {
		return beanStore.get( clazz );
	}

	/**
	 * {@inheritDoc}
	 */
	public void injectStatics(Class<?> clazz) {
		InjectionContext context = new InjectionContextImpl( beanStore, true );
		beanStore.getInjector().injectStatics( context, clazz );
	}

	/**
	 * {@inheritDoc}
	 */
	public void injectDependencies(Object bean) {
		InjectionContext context = new InjectionContextImpl( beanStore, true );
		beanStore.getInjector().injectInstance( context, bean );
	}

	/**
	 * {@inheritDoc}
	 */
	private void registerShutdownHook() {
		Thread thread = new Thread( this.new CallbackShutdownHook( this.beanStore ) );
		Runtime.getRuntime().addShutdownHook( thread );
	}


	/**
	 * Shutdown hook inner class.
	 */
	private class CallbackShutdownHook implements Runnable {

		private final Logger logger = LoggerFactory.getLogger( CallbackShutdownHook.class );

		private final BeanStore context;

		/**
		 * The CallbackShutdown hook beanStore.
		 *
		 * @param context the bean beanStore
		 */
		public CallbackShutdownHook(BeanStore context) {
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

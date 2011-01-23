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
package com.my.container.core;

import com.my.container.core.spi.InjectorProvider;
import com.my.container.util.loader.ServiceLoader;

/**
 * The Context Interface
 *
 * @author Kevin Pollet
 */
public abstract class Injector {

	/**
	 * Hold an instance of the first
	 * loaded injector provider.
	 */
	private static InjectorProvider injectorProvider;

	//Load first injector provider
	static {
		ServiceLoader<InjectorProvider> providers = ServiceLoader.load( InjectorProvider.class );
		if ( providers.isServiceLoaded() ) {
			throw new InjectorProviderNotFoundException( "There is no provider injection in the classpath" );
		}
		else {
			for ( InjectorProvider provider : providers ) {
				injectorProvider = provider;
				break;
			}
		}
	}

	/**
	 * Create a configuration object to set properties
	 * on an injector. This injector can be build from
	 * this configuration object.
	 *
	 * @return the configuration object
	 */
	public static Configuration configure() {
		return injectorProvider.configure();
	}

	/**
	 * Get a bean in the container Context.
	 *
	 * @param clazz the bean class
	 *
	 * @return the new bean instance
	 */
	public abstract <T> T getBean(Class<T> clazz);

	/**
	 * Inject statics member and methods.
	 *
	 * @param clazz The class to be injected
	 */
	public abstract void injectStatics(Class<?> clazz);

	/**
	 * This method is used to inject dependencies in an
	 * existing bean instance (Method and fields).
	 *
	 * @param bean the instance to be injected
	 */
	public abstract void injectDependencies(Object bean);
}

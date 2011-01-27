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
package com.my.container;

import com.my.container.spi.ContainerProvider;
import com.my.container.loader.ServiceLoader;

/**
 * The Container provider interface.
 *
 * @author Kevin Pollet
 */
public abstract class Container {

	//TODO provide an other bootstrap mechanism
	/**
	 * Hold an instance of the first
	 * loaded container provider.
	 */
	private static ContainerProvider provider;

	//Load first injector provider
	static {
		ServiceLoader<ContainerProvider> providers = ServiceLoader.load( ContainerProvider.class );
		if ( providers.isServiceLoaded() ) {
			throw new ProviderNotFoundException( "There is no provider injection in the classpath" );
		}
		else {
			for ( ContainerProvider provider : providers ) {
				Container.provider = provider;
				break;
			}
		}
	}

	/**
	 * Create a configuration object to set properties
	 * of a container. The configured container can be built from
	 * this object.
	 *
	 * @return the configuration object
	 */
	public static Configuration configure() {
		return provider.configure();
	}

	/**
	 * Get a bean in the container Context.
	 *
	 * @param clazz the bean class
	 *
	 * @return the new bean instance
	 */
	public abstract <T> T get(Class<T> clazz);

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

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
package com.my.container.bootstrap;

import com.my.container.NoContainerProviderFoundException;
import com.my.container.util.ServiceLoader;
import com.my.container.spi.ContainerProvider;

/**
 * @author Kevin Pollet
 */
public final class Bootstrap {
	/**
	 * Hold an instance of the first loaded container loadedProvider.
	 */
	private static ContainerProvider loadedProvider;

	/**
	 * Load an instance of a container provider.
	 *
	 * @return the container provider instance
	 */
	public synchronized static ContainerProvider loadProvider() {
		if ( loadedProvider == null ) {
			ServiceLoader<ContainerProvider> providers = ServiceLoader.load( ContainerProvider.class );
			if ( providers.isServiceLoaded() ) {
				throw new NoContainerProviderFoundException( "There is no container provider in the classpath" );
			}
			else {
				for ( ContainerProvider provider : providers ) {
					loadedProvider = provider;
					break;
				}
			}
		}
		return loadedProvider;
	}

	/**
	 * Retrieve the current loaded container provider.
	 *
	 * @return the container provider
	 */
	public synchronized static ContainerProvider currentContainerProvider() {
		return loadedProvider;
	}
}

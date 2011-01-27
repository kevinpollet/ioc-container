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

import com.my.container.binding.BindingProvider;
import com.my.container.bootstrap.Bootstrap;

/**
 * @author Kevin Pollet
 */
public abstract class Configuration {
	/**
	 * Add a binding provider
	 *
	 * @param provider the provider to add
	 * @param <T> the provider type
	 *
	 * @return Configuration interface for fluent configuration
	 */
	public abstract <T extends BindingProvider> Configuration addBindingProvider(T provider);

	/**
	 * Add a shutdown hook. When JVM is destroyed
	 * the shutdown will called all preDestroy callback.
	 *
	 * @param enable true to add a shutdown.
	 *
	 * @return Configuration interface for fluent configuration
	 */
	public abstract Configuration shutDownHook(boolean enable);

	/**
	 * Access to provider specific configuration object.
	 *
	 * @param clazz the class
	 * @param <T> the type of the class
	 *
	 * @return the provider specific configuration
	 */
	public abstract <T extends Configuration> T unwrap(Class<T> clazz);

	/**
	 * Get a configured instance of a container.
	 *
	 * @return a configured instance of container
	 */
	public Container buildContainer() {
		return Bootstrap.currentContainerProvider().buildContainer( this );
	}
}

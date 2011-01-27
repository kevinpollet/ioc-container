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
package com.my.container.spi;

import com.my.container.Configuration;
import com.my.container.Container;

/**
 * @author Kevin Pollet
 */
public interface ContainerProvider {
	/**
	 * Configure a container.
	 *
	 * @return a configuration element
	 */
	Configuration configure();

	/**
	 * Use specific provider container configuration.
	 *
	 * @param clazz the specific container configuration class
	 *
	 * @return the specific container provider configuration
	 */
	<T extends Configuration> T useSpecificConfiguration(Class<T> clazz);

	/**
	 * Get a container instance from this container
	 * provider with the given configuration.
	 *
	 * @param configuration the container configuration
	 * @return a configured instance of container
	 */
	Container buildContainer(Configuration configuration);
}

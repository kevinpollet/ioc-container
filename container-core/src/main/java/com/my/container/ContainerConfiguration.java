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

/**
 * @author Kevin Pollet
 */
public interface ContainerConfiguration extends Configuration<ContainerConfiguration> {
	/**
	 * Add a shutdown hook. When JVM is destroyed
	 * the shutdown will called all preDestroy callback.
	 *
	 * @param enable true to add a shutdown.
	 *
	 * @return Configuration interface for fluent configuration
	 */
	ContainerConfiguration shutDownHook(boolean enable);
}

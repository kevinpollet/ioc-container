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
public interface ContextBeanStore {
	/**
	 * Get the ben instance stored in this bean store.
	 * If none this store have to created the bean
	 * with an Injector.
	 *
	 * @param clazz
	 * @param <T>
	 */
	<T> T get(Class<T> clazz);

	/**
	 * Get this bean store injector.
	 *
	 * @return the injector
	 */
	Injector getInjector();

	/**
	 * Destroy this context bean store.
	 */
	void destroy();
}

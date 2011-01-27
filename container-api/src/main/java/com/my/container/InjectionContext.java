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
 * The injection context used during the injection
 * mechanism.
 *
 * @author Kevin Pollet
 */
public interface InjectionContext {
	/**
	 * Inject only static methods and field.
	 *
	 * @return true if only static fields or methods have to be injected
	 */
	boolean isStaticInjection();

	/**
	 * Get the context bean factory.
	 *
	 * @return the context bean factory
	 */
	ContextBeanFactory getContextBeanFactory();

	/**
	 * Check if the given class have been already processed
	 * in the current dependency injection.
	 *
	 * @return true if the class have been already processed
	 */
	boolean isAlreadyProcessed(Class<?> clazz);

	/**
	 * Mark a class as processed in the current dependency injection
	 * resolution.
	 *
	 * @param clazz the processed class
	 * @param result the result of this computation
	 */
	void markClassAsProcessed(Class<?> clazz, Object result);

	/**
	 * Get the result of the
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T getResultOfProcessionFor(Class<T> clazz);

	/**
	 * Remove processed class mark.
	 *
	 * @param clazz the class.
	 */
	void removeMarkFor(Class<?> clazz);
}

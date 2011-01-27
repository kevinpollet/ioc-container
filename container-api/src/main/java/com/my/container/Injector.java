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
public interface Injector {
	/**
	 * Construct an instance of the given class within the
	 * given context and resolve it's dependencies.
	 *
	 * @param context the injection context
	 * @param clazz the class to construct
	 * @param <T> the class type
	 *
	 * @return an instance of the bean
	 */
	<T> T constructClass(InjectionContext context, Class<T> clazz);

	/**
	 * Inject only static field and method of the given
	 * class within the given context.
	 *
	 * @param context the injection context
	 * @param clazz the class
	 */
	void injectStatics(InjectionContext context, Class<?> clazz);

	/**
	 * Inject the given instance (resolve it's dependencies) within
	 * the given context.
	 *
	 * @param context the injection context
	 * @param instance the instance to inject
	 */
	void injectInstance(InjectionContext context, Object instance);
}

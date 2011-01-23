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
package com.my.container.binding;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This interface is the contract for
 * a binding holder.
 *
 * @author Kevin Pollet
 */
public interface BindingHolder {

	/**
	 * Put a bindings in this binding collection.
	 *
	 * @param binding the binding
	 *
	 * @throws IllegalArgumentException if binding is null
	 */
	void put(final Binding<?> binding);

	/**
	 * There is a binding for the given class.
	 *
	 * @param clazz the class
	 *
	 * @return true if one, false otherwise
	 */
	boolean isBindingFor(final Class<?> clazz);

	/**
	 * Retrieve all binding defined to the given class.
	 *
	 * @param clazz the class
	 * @param <T> the type of the binding
	 *
	 * @return null if none or the binding list
	 *
	 * @throws IllegalArgumentException if clazz is null
	 */
	<T> Binding<T> getBindingFor(final Class<T> clazz);

	/**
	 * Get the binding defined for the given class with
	 * the given qualifier. if the qualifier parameter
	 * is null have the same effect of calling the
	 * {@link com.my.container.binding.BindingHolder#getBindingFor(Class)}
	 * method.
	 *
	 * @param clazz the class
	 * @param qualifier the binding qualifier
	 * @param <Q> the qualifier type
	 *
	 * @return null if none or the binding
	 *
	 * @throws IllegalArgumentException if clazz is null or qualifier is not annotated with @Qualifier
	 */
	<Q extends Annotation> Binding<?> getBindingFor(final Class<?> clazz, final Q qualifier);

	/**
	 * Remove all the bindings for the given class.
	 *
	 * @param clazz the class
	 *
	 * @return null if none or the list removed
	 */
	List<Binding<?>> removeAllBindingFor(final Class<?> clazz);

	/**
	 * Remove the qualified binding for
	 * the given class.
	 *
	 * @param clazz the class
	 * @param qualifier the qualifier
	 * @param <Q> the qualifier type
	 *
	 * @return null if none or the biding removed
	 *
	 * @throws IllegalArgumentException if clazz is null or qualifier is not annotated with @Qualifier or null
	 */
	<Q extends Annotation> Binding<?> removeQualifiedBindingFor(final Class<?> clazz, final Q qualifier);

	/**
	 * Remove all bindings contains
	 * in this Collection.
	 */
	void removeAllBindings();

}

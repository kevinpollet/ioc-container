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
package com.my.container.binding;

import java.lang.annotation.Annotation;
import javax.inject.Provider;

/**
 * The binding definition Class.
 *
 * @author Kevin Pollet
 */
public class Binding<T> {

	private Class<T> clazz;

	private Class<? extends T> impl;

	private Class<? extends Provider<T>> provider;

	/**
	 * The named qualifier.
	 *
	 * @see javax.inject.Named
	 */
	private String name;

	/**
	 * The qualifier. A qualifier is an
	 * annotation annotated with @Qualifier.
	 *
	 * @see javax.inject.Qualifier
	 */
	private Class<? extends Annotation> qualifier;

	/**
	 * Get the qualifier for this binding.
	 *
	 * @return the qualifier
	 */
	public Class<? extends Annotation> getQualifier() {
		return this.qualifier;
	}

	/**
	 * Get the binding class.
	 *
	 * @return the biding class
	 */
	public Class<T> getClazz() {
		return this.clazz;
	}

	/**
	 * Get the binding implementation class.
	 *
	 * @return the implementation class
	 */
	public Class<? extends T> getImplementation() {
		return this.impl;
	}

	/**
	 * Get the binding Named qualifier value.
	 *
	 * @return the value of the name qualifier or null if none
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the binding class.
	 *
	 * @param clazz the binding class
	 */
	public void setClazz(final Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Set the binding implementation class.
	 *
	 * @param impl the implementation class
	 */
	public void setImplementation(final Class<? extends T> impl) {
		this.impl = impl;
	}

	/**
	 * Set the binding name qualifier value
	 *
	 * @param name the name of the name qualifier value
	 */
	public void setNamed(final String name) {
		this.name = name;
	}

	/**
	 * Set the binding qualifier.
	 *
	 * @param qualifier the qualifier of the binding or null if none
	 */
	public void setQualifier(final Class<? extends Annotation> qualifier) {
		this.qualifier = qualifier;
	}

	/**
	 * Get the provider.
	 *
	 * @return the provider
	 */
	public Class<? extends Provider<T>> getProvider() {
		return this.provider;
	}

	/**
	 * Set the provider.
	 *
	 * @param provider the provider
	 */
	public void setProvider(final Class<? extends Provider<T>> provider) {
		this.provider = provider;
	}
}

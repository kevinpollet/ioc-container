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
 * @author Kevin Pollet
 */
public abstract class FluentBindingProvider extends BindingProvider {

	/**
	 * Bind a class.
	 *
	 * @param clazz the class to bind
	 */
	protected final void bindClass(Class<?> clazz) {
		addBinding( new Binding( clazz ) );
	}

	/**
	 * Create a binding for the given class.
	 *
	 * @param clazz the binding class
	 *
	 * @return the binding builder
	 */
	protected final <T> FluentBinder<T> bind(Class<T> clazz) {
		return this.new FluentBinder( clazz );
	}

	/**
	 * The basic binder inner class.
	 */
	protected final class FluentBinder<T> {

		private final Class<T> clazz;

		/**
		 * Construct a FluentBinder to create
		 * a binding for the given class.
		 *
		 * @param clazz the clazz to be bind
		 */
		private FluentBinder(Class<T> clazz) {
			this.clazz = clazz;
		}

		/**
		 * The binding implementation.
		 *
		 * @param impl the implementation class
		 */
		public final FluentQualifierBinder to(Class<? extends T> impl) {
			Binding<T> binding = new Binding<T>( clazz, impl );
			FluentBindingProvider.this.addBinding( binding );
			return FluentBindingProvider.this.new FluentQualifierBinder( binding );
		}

		/**
		 * The binding provider
		 *
		 * @param provider the provider class
		 */
		public final FluentQualifierBinder toProvider(Class<? extends Provider<T>> provider) {
			Binding<T> binding = new ProvidedBinding<T>( clazz, provider );
			FluentBindingProvider.this.addBinding( binding );
			return FluentBindingProvider.this.new FluentQualifierBinder( binding );
		}

	}

	/**
	 * The qualifier binder inner class.
	 */
	protected final class FluentQualifierBinder<T> {

		private final Binding<T> binding;

		/**
		 * Construct a Fluent qualifier binder.
		 *
		 * @param binding the binding to qualify.
		 */
		private FluentQualifierBinder(Binding<T> binding) {
			this.binding = binding;
		}

		/**
		 * Add the {@link javax.inject.Named} qualifier
		 * to this binding.
		 *
		 * @param named the Named qualifier binding named
		 */
		public final void named(String named) {
			binding.setNamed( named );
		}

		/**
		 * Add a qualifier to this binding.
		 *
		 * @param qualifier the binding qualifier
		 */
		public final void qualifiedBy(Class<? extends Annotation> qualifier) {
			binding.setQualifier( qualifier );
		}
	}

}

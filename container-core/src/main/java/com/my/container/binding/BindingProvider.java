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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The BindingProvider class. A Binding provider
 * is responsible to share the bindings.
 *
 * @author Kevin Pollet
 */
public abstract class BindingProvider {

	/**
	 * The binding list.
	 */
	private final List<Binding<?>> bindings;

	/**
	 * Binding provider constructor.
	 */
	protected BindingProvider() {
		this.bindings = new ArrayList<Binding<?>>();
	}

	/**
	 * Populate the binding list.
	 */
	public abstract void configureBindings();

	/**
	 * Get the binding provider list.
	 */
	public final List<Binding<?>> getBindings() {
		return Collections.unmodifiableList( bindings );
	}

	/**
	 * Add a binding to the binding list.
	 *
	 * @param binding the binding to add
	 */
	protected final void addBinding(Binding<?> binding) {
		bindings.add( binding );
	}

}

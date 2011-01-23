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
package com.my.container.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.core.Configuration;
import com.my.container.core.Injector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Kevin Pollet
 */
class InjectorConfiguration implements Configuration {

	private boolean shutDownHookEnable;

	private List<BindingProvider> providers;

	public InjectorConfiguration() {
		this.shutDownHookEnable = false;
		this.providers = new ArrayList<BindingProvider>();
	}

	public <T extends BindingProvider> Configuration addBindingProvider(T provider) {
		providers.add( provider );
		return this;
	}

	public Configuration addXmlBindingProvider(String providerFileLocation) {
		throw new NotImplementedException();
	}

	public Configuration enableShutDownHook(boolean enable) {
		shutDownHookEnable = enable;
		return this;
	}

	public boolean isShutDownHookEnable() {
		return shutDownHookEnable;
	}

	public List<BindingProvider> getBindingProviders() {
		return providers;
	}

	public Injector buildInjector() {
		return new InjectorImpl( this );
	}
}

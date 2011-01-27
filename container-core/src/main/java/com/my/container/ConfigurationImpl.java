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

import java.util.ArrayList;
import java.util.List;

import com.my.container.binding.BindingProvider;
import com.my.container.bootstrap.Bootstrap;

/**
 * @author Kevin Pollet
 */
public class ConfigurationImpl implements SpecificConfiguration {

	private boolean shutDownHookEnable;

	private List<BindingProvider> providers;

	public ConfigurationImpl() {
		this.shutDownHookEnable = false;
		this.providers = new ArrayList<BindingProvider>();
	}

	public <T extends BindingProvider> SpecificConfiguration addBindingProvider(T provider) {
		providers.add( provider );
		return this;
	}

	public SpecificConfiguration shutDownHook(boolean enable) {
		shutDownHookEnable = enable;
		return this;
	}

	public Container buildContainer() {
		return Bootstrap.currentContainerProvider().buildContainer( this );
	}

	public boolean isShutDownHookEnable() {
		return shutDownHookEnable;
	}

	public List<BindingProvider> getBindingProviders() {
		return providers;
	}

}

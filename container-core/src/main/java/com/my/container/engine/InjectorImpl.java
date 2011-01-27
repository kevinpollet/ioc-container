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
package com.my.container.engine;

import com.my.container.InjectionContext;
import com.my.container.Injector;
import com.my.container.engine.injector.ConstructorInjector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Kevin Pollet
 */
public class InjectorImpl implements Injector {

	private ConstructorInjector constructorInjector;

	public InjectorImpl() {
		this.constructorInjector = new ConstructorInjector();
	}

	public <T> T constructClass(InjectionContext context, Class<T> clazz) {
		return constructorInjector.constructClass( context, clazz );
	}

	public void injectStatics(InjectionContext context, Class<?> clazz) {
		throw new NotImplementedException();
	}

	public void injectInstance(InjectionContext context, Object instance) {
		constructorInjector.injectDependencies( context, instance );
	}

}

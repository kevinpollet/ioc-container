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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import com.my.container.ContextBeanStore;
import com.my.container.InjectionContext;
import com.my.container.Injector;
import com.my.container.NoSuchBeanDefinitionException;
import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.binding.MapBindingHolder;
import com.my.container.binding.ProvidedBinding;

import static com.my.container.util.ReflectionHelper.getMethodAnnotatedWith;
import static com.my.container.util.ReflectionHelper.invokeMethod;
import static com.my.container.util.ValidationHelper.isValidCallbackMethod;

/**
 * The factory of bean class
 *
 * @author kevinpollet
 */
public final class ContextBeanStoreImpl implements ContextBeanStore {

	private final BindingHolder holder;

	private final BindingHolder providerHolder;

	private final List<Object> prototypeBean;

	private final Map<Class<?>, Object> singletonBeans;


	/**
	 * Bean Factory constructor.
	 *
	 * @param list the binding list
	 */
	public ContextBeanStoreImpl(final List<Binding<?>> list) {
		this.holder = new MapBindingHolder();
		this.providerHolder = new MapBindingHolder();

		this.prototypeBean = new ArrayList<Object>();
		this.singletonBeans = new HashMap<Class<?>, Object>();

		//Populate holder
		if ( list != null ) {
			for ( Binding b : list ) {
				if ( b instanceof ProvidedBinding ) {
					this.providerHolder.put( b );
				}
				else {
					this.holder.put( b );
				}
			}
		}
	}

	public <T> T get(Class<T> clazz) {
        if ( clazz == null ) {
			throw new IllegalArgumentException( "The clazz parameter cannot be null" );
		}
		else if ( !this.holder.isBindingFor( clazz ) ) {
			throw new NoSuchBeanDefinitionException(
					String.format(
							"The class %s have no binding defined", clazz.getSimpleName()
					)
			);
		}

		T createdBean;
		Binding<T> binding = this.holder.getBindingFor( clazz );
		Class<? extends T> toClass = binding.getImplementation();

		if ( binding.getImplementation().isAnnotationPresent( Singleton.class ) && this.singletonBeans
				.containsKey( clazz ) ) {
			return clazz.cast( this.singletonBeans.get( toClass ) );
		}

		InjectionContext context = new InjectionContextImpl( this, false );
		createdBean = getInjector().constructClass( context, toClass );

		return createdBean;
	}

	public <T> void put(Class<T> clazz, T Object) {
		if ( Object.getClass().isAnnotationPresent( Singleton.class ) ) {
			singletonBeans.put( clazz, Object );
		}
		prototypeBean.add( Object );
	}

	public Injector getInjector() {
		return new InjectorImpl();
	}

	/**
	 * This method remove all beans references
	 * memorised by the singleton for PreDestroy
	 * CallBack and Singleton scope.
	 */
	public void destroy() {
		List<Object> createdBean = new ArrayList<Object>();
		createdBean.addAll( prototypeBean );
		createdBean.addAll( singletonBeans.values() );

		//Call PreDestroy callbacks
		for ( Object bean : createdBean ) {
			Method method = getMethodAnnotatedWith( PreDestroy.class, bean.getClass() );
			if ( method!= null && isValidCallbackMethod( method ) ) {
				if ( !method.isAccessible() ) {
					method.setAccessible( true );
				}
				try {
					invokeMethod( bean, method );
				}
				catch ( RuntimeException ex ) {
					//ignore runtime exception - see JSR250 PreDestroy method
				}
			}
		}
	}


	public  Map<Class<?>, Object> getSingletonBeans() {
		return singletonBeans;
	}

	/**
	 * Get the binding holder.
	 *
	 * @return the binding holder
	 */
	public BindingHolder getBindingHolder() {
		return holder;
	}

	/**
	 * Get the provided binding holder.
	 *
	 * @return the provided binding holder
	 */
	public BindingHolder getProviderHolder() {
		return providerHolder;
	}

}

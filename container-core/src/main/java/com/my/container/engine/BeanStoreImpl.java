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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import com.my.container.BeanStore;
import com.my.container.InjectionContext;
import com.my.container.Injector;
import com.my.container.NoSuchBeanDefinitionException;
import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.binding.MapBindingHolder;
import com.my.container.util.ContractsHelper;

import static com.my.container.util.ReflectionHelper.getMethodAnnotatedWith;
import static com.my.container.util.ReflectionHelper.invokeMethod;
import static com.my.container.util.ValidationHelper.isValidCallbackMethod;

/**
 * The factory of bean class
 *
 * @author kevinpollet
 */
public final class BeanStoreImpl implements BeanStore {

	private final BindingHolder holder;

	private final BindingHolder providerHolder;

	private final List<Object> prototypeBean;

	private final Map<Class<?>, Object> singletonBeans;

	private final Injector injector;


	/**
	 * Bean Factory constructor.
	 *
	 * @param list the binding list
	 */
	public BeanStoreImpl(final List<Binding<?>> list) {
		this.holder = new MapBindingHolder();
		this.providerHolder = new MapBindingHolder();
		this.prototypeBean = new ArrayList<Object>();
		this.singletonBeans = new HashMap<Class<?>, Object>();
		this.injector = new InjectorImpl();

		//Populate holder
		if ( list != null ) {
			for ( Binding b : list ) {
				if ( b.getProvider() != null ) {
					this.providerHolder.put( b );
				}
				else {
					this.holder.put( b );
				}
			}
		}
	}

	public <T> T get(Class<T> clazz) {
		ContractsHelper.assertNoNull( clazz, "clazz" );

		if ( !holder.isBindingFor( clazz ) ) {
			throw new NoSuchBeanDefinitionException(
					String.format(
							"The class %s have no binding defined", clazz.getSimpleName()
					)
			);
		}

		T createdBean;
		Binding<T> binding = holder.getBindingFor( clazz );
		Class<? extends T> toClass = binding.getImplementation();

		if ( toClass.isAnnotationPresent( Singleton.class ) && singletonBeans.containsKey( clazz ) ) {

			createdBean = clazz.cast( singletonBeans.get( toClass ) );
		}
		else {
		    InjectionContext context = new InjectionContextImpl( this, false );
			createdBean = injector.constructClass( context, toClass );
		}

		return createdBean;
	}

	public <T> void put(T bean) {
		ContractsHelper.assertNoNull( bean, "bean" );

		if ( bean.getClass().isAnnotationPresent( Singleton.class ) ) {
			singletonBeans.put( bean.getClass(), bean );
		}
		else if ( getMethodAnnotatedWith( PreDestroy.class, bean.getClass() ) != null ) {
			prototypeBean.add( bean );
		}

		postConstruct( bean );
	}

	public Injector getInjector() {
		return injector;
	}

	public void destroy() {
		List<Object> createdBean = new ArrayList<Object>();
		createdBean.addAll( prototypeBean );
		createdBean.addAll( singletonBeans.values() );

		for ( Object bean : createdBean ) {
			preDestroy( bean );
		}
	}

	private void postConstruct(Object object) {
		Method postConstructMethod = getMethodAnnotatedWith( PostConstruct.class, object.getClass() );
		if ( postConstructMethod != null && isValidCallbackMethod( postConstructMethod ) ) {
			if ( !postConstructMethod.isAccessible() ) {
				postConstructMethod.setAccessible( true );
			}
			invokeMethod( object, postConstructMethod );
		}
	}

	private void preDestroy(Object object) {
		Method postConstructMethod = getMethodAnnotatedWith( PreDestroy.class, object.getClass() );
		if ( postConstructMethod != null && isValidCallbackMethod( postConstructMethod ) ) {
			if ( !postConstructMethod.isAccessible() ) {
				postConstructMethod.setAccessible( true );
			}
			invokeMethod( object, postConstructMethod );
		}
	}

	public Map<Class<?>, Object> getSingletonBeans() {
		return singletonBeans;
	}

	public BindingHolder getBindingHolder() {
		return holder;
	}

	public BindingHolder getProviderHolder() {
		return providerHolder;
	}
}

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
package com.my.container.engine.injector;

import com.my.container.BeanDependencyInjectionException;
import com.my.container.InjectionContext;
import com.my.container.NoSuchBeanDefinitionException;
import com.my.container.engine.BeanStoreImpl;
import com.my.container.binding.Binding;
import com.my.container.binding.ProvidedBinding;
import com.my.container.engine.DefaultInstanceProvider;
import com.my.container.util.ProxyHelper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

/**
 * Definition an Injector of fields.
 *
 * @author Kevin Pollet
 */
public class FieldInjector {

	/**
	 * Inject dependency field annotated by @Inject.
	 *
	 * @param context the injection engine
	 * @param clazz the current class injected
	 * @param instance the bean instance injected
	 */
	public static void injectFieldsDependencies(final InjectionContext context, final Class<?> clazz, final Object instance) {

		for ( Field field : clazz.getDeclaredFields() ) {

			if ( field.isAnnotationPresent( Inject.class ) ) {

				Object fieldInstance = null;
				Annotation qualifier = null;
				Binding<?> injectionBinding;
				Class<?> fieldClass = field.getType();

				if ( Modifier.isFinal( field.getModifiers() ) ) {
					throw new BeanDependencyInjectionException(
							String.format(
									"Cannot inject final field %s in class %s", field.getName(), clazz.getName()
							)
					);
				}

				//Class have dependencies mark it
				context.markClassAsProcessed( instance.getClass(), instance );

				//Get qualifier
				for ( Annotation annotation : field.getAnnotations() ) {
					if ( annotation.annotationType().isAnnotationPresent( Qualifier.class ) ) {
						qualifier = annotation;
						break;
					}
				}

				if ( fieldClass.isAssignableFrom( Provider.class ) ) {
					if ( field.getGenericType() instanceof ParameterizedType ) {
						Class<?> classToInject = (Class<?>) ( (ParameterizedType) field.getGenericType() ).getActualTypeArguments()[0];
						injectionBinding = ((BeanStoreImpl) context.getBeanStore())
								.getProviderHolder()
								.getBindingFor( classToInject, qualifier );
						if ( injectionBinding == null ) {
							injectionBinding = ((BeanStoreImpl) context.getBeanStore())
									.getBindingHolder()
									.getBindingFor( classToInject, qualifier );
							if ( injectionBinding == null ) {
								throw new NoSuchBeanDefinitionException(
										String.format(
												"There is no binding defined for the class %s", classToInject.getName()
										)
								);
							}
							fieldInstance = new DefaultInstanceProvider(
									((BeanStoreImpl) context.getBeanStore()), injectionBinding.getImplementation()
							);
						}
						else {
							fieldInstance = context.getBeanStore().getInjector().constructClass( context, ( (ProvidedBinding) injectionBinding ).getProvider() );
						}
					}
				}
				else {
					injectionBinding = ((BeanStoreImpl) context.getBeanStore())
							.getBindingHolder()
							.getBindingFor( field.getType(), qualifier );
					if ( injectionBinding == null ) {
						throw new NoSuchBeanDefinitionException(
								String.format(
										"There is no binding defined for the class %s", field.getType().getName()
								)
						);
					}
					fieldInstance = context.getBeanStore().getInjector().constructClass( context, injectionBinding.getImplementation() );
				}

				//Set field instance
				try {
					if ( !field.isAccessible() ) {
						field.setAccessible( true );
					}
					field.set( ProxyHelper.getTargetObject( instance ), fieldInstance );
				}
				catch ( IllegalAccessException ex ) {
					throw new BeanDependencyInjectionException(
							String.format(
									"The field %s is not accessible and cannot be injected", field.getName()
							), ex
					);
				}

				context.removeMarkFor( instance.getClass() );
			}
		}
	}

}

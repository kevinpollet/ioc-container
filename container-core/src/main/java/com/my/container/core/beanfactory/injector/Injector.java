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
package com.my.container.core.beanfactory.injector;

import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.binding.ProvidedBinding;
import com.my.container.core.beanfactory.BeanFactory;
import com.my.container.core.beanfactory.exceptions.BeanDependencyInjectionException;
import com.my.container.core.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.core.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.spi.BeanProcessor;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Definition of an injector. This injector
 * can create an instance class and inject
 * dependencies into an existing bean.
 *
 * @author Kevin Pollet
 */
public class Injector {

	private final Logger logger = LoggerFactory.getLogger( Injector.class );

	private final FieldInjector fieldInjector;

	private final MethodInjector methodInjector;

	/**
	 * Construct an Injector instance.
	 */
	public Injector() {
		this.fieldInjector = new FieldInjector( this );
		this.methodInjector = new MethodInjector( this );
	}

	/**
	 * Construct an instance of the given class and resolve
	 * dependencies of the newly created instance.
	 *
	 * @param context the injection core
	 * @param clazz the class
	 * @param <T> the type of the class
	 *
	 * @return an instance of the class with it's properties resolved
	 */
	public <T> T constructClass(final InjectionContext context, final Class<T> clazz) {
		this.logger.debug( "Construct an instance of class {}", clazz.getSimpleName() );

		T classInstance = null;
		BeanFactory factory = context.getBeanFactory();
		BindingHolder holder = factory.getBindingHolder();
		BindingHolder providerHolder = factory.getProviderHolder();

		//Check if bean is a singleton and have been already created
		if ( factory.getSingletonBeans().containsKey( clazz ) ) {
			return clazz.cast( context.getBeanFactory().getSingletonBeans().get( clazz ) );
		}

		//Check if there is a cyclic dependency reference
		if ( context.getCyclicHandlerMap().containsKey( clazz ) ) {
			Object objectRef = context.getCyclicHandlerMap().get( clazz );
			if ( objectRef == null ) {
				throw new BeanDependencyInjectionException( "Cyclic dependencies in constructor are not valid." );
			}
			return clazz.cast( objectRef );
		}

		//Construct an instance of the given class
		try {

			for ( Constructor<?> constructor : clazz.getDeclaredConstructors() ) {

				if ( constructor.isAnnotationPresent( Inject.class ) ) {

					Class<?>[] parametersClass = constructor.getParameterTypes();
					Type[] parametersTypes = constructor.getGenericParameterTypes();
					Object[] parameters = new Object[parametersTypes.length];
					Annotation[][] parametersAnnotations = constructor.getParameterAnnotations();

					context.getCyclicHandlerMap().put( clazz, null );

					for ( int i = 0; i < parametersTypes.length; i++ ) {

						Annotation qualifier = null;
						Binding injectionBinding = null;
						Type parameterType = parametersTypes[i];
						Class<?> classToInject = parametersClass[i];

						//Get qualifier
						for ( Annotation annotation : parametersAnnotations[i] ) {
							if ( annotation.annotationType().isAnnotationPresent( Qualifier.class ) ) {
								qualifier = annotation;
								break;
							}
						}

						//Provider Injection
						if ( classToInject.isAssignableFrom( Provider.class ) ) {

							if ( parameterType instanceof ParameterizedType ) {
								classToInject = (Class<?>) ( (ParameterizedType) parameterType ).getActualTypeArguments()[0];

								//Check if user provider
								injectionBinding = providerHolder.getBindingFor( classToInject, qualifier );
								if ( injectionBinding == null ) {
									injectionBinding = holder.getBindingFor( classToInject, qualifier );
									if ( injectionBinding == null ) {
										throw new NoSuchBeanDefinitionException(
												String.format(
														"There is no binding defined for the class %s",
														classToInject.getName()
												)
										);
									}
									parameters[i] = new DefaultInstanceProvider(
											factory, injectionBinding.getImplementation()
									);

								}
								else {
									parameters[i] = this.constructClass(
											context, ( (ProvidedBinding) injectionBinding ).getProvider()
									);
								}
							}

							//Normal injection
						}
						else {
							injectionBinding = holder.getBindingFor( classToInject, qualifier );
							if ( injectionBinding == null ) {
								throw new NoSuchBeanDefinitionException(
										String.format(
												"There is no binding defined for the class %s", classToInject.getName()
										)
								);
							}
							parameters[i] = this.constructClass( context, injectionBinding.getImplementation() );
						}
					}

					if ( !Modifier.isPrivate( constructor.getModifiers() )
							&& !constructor.isAccessible() ) {
						constructor.setAccessible( true );
					}

					classInstance = clazz.cast( constructor.newInstance( parameters ) );
					context.getCyclicHandlerMap().remove( clazz );
					break;
				}

			}

			if ( classInstance == null ) {
				classInstance = clazz.newInstance();
			}

			//Apply bean post processor
			try {
				for ( BeanProcessor processor : factory.getBeanProcessors() ) {
					if ( processor.isProcessable( classInstance ) ) {
						classInstance = processor.processBean( classInstance );
					}
				}
			}
			catch ( Exception ex ) {
				throw new BeanInstantiationException( "Error when applying bean post processors", ex );
			}

			//Hold singletons and prototypes bean
			if ( clazz.isAnnotationPresent( Singleton.class ) ) {
				factory.getSingletonBeans().put( clazz, classInstance );
			}
			else if ( ReflectionHelper.isCallbackMethod( clazz ) ) {
				factory.getPrototypeBeans().add( classInstance );
			}

			this.injectFieldAndMethod( context, clazz, classInstance );

			//Hold created bean
			context.getNewlyCreatedBeans().add( classInstance );

		}
		catch ( Exception ex ) {
			throw new BeanInstantiationException(
					String.format(
							"The %s class cannot be instantiated", clazz.getName()
					), ex
			);
		}

		return classInstance;
	}

	/**
	 * Inject dependencies into an existing beans.
	 *
	 * @param context the injection core
	 * @param bean the bean to be injected
	 */
	public void injectDependencies(final InjectionContext context, final Object bean) {
		this.logger.debug( "Inject dependencies in instance of class {}", bean.getClass().getSimpleName() );
		this.injectFieldAndMethod( context, bean.getClass(), bean );
	}

	/**
	 * Inject fields and method in a bean instance. Fields and Methods
	 * in SuperClass are injected first.
	 *
	 * @param context the injection core
	 * @param clazz the current class injected
	 * @param instance the instance where values are injected
	 */
	private void injectFieldAndMethod(final InjectionContext context, final Class<?> clazz, final Object instance) {
		Class<?> superClass = clazz.getSuperclass();

		if ( !superClass.equals( Object.class ) ) {
			this.injectFieldAndMethod( context, superClass, instance );
		}

		this.fieldInjector.injectFieldsDependencies( context, clazz, instance );
		this.methodInjector.injectMethodsDependencies( context, clazz, instance );
	}

}

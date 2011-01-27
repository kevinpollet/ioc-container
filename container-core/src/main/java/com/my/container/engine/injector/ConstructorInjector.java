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

import com.my.container.BeanInstantiationException;
import com.my.container.InjectionContext;
import com.my.container.BeanDependencyInjectionException;
import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.binding.ProvidedBinding;
import com.my.container.engine.ContextBeanStoreImpl;
import com.my.container.NoSuchBeanDefinitionException;
import com.my.container.spi.BeanProcessor;
import com.my.container.engine.provider.GenericProvider;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static com.my.container.util.ReflectionHelper.getMethodAnnotatedWith;
import static com.my.container.util.ReflectionHelper.invokeMethod;
import static com.my.container.util.ReflectionHelper.isMethodAnnotatedWith;
import static com.my.container.util.ValidationHelper.isValidCallbackMethod;

/**
 * Definition of an injector. This injector
 * can create an instance class and inject
 * dependencies into an existing bean.
 *
 * @author Kevin Pollet
 */
public class ConstructorInjector {

	private final Logger logger = LoggerFactory.getLogger( ConstructorInjector.class );

	private final List<BeanProcessor> beanProcessors;

	private final FieldInjector fieldInjector;

	private final MethodInjector methodInjector;

	/**
	 * Construct an Injector instance.
	 */
	public ConstructorInjector(List<BeanProcessor> beanProcessors) {
		this.fieldInjector = new FieldInjector( this );
		this.methodInjector = new MethodInjector( this );
		this.beanProcessors= beanProcessors;
	}

	/**
	 * Construct an instance of the given class and resolve
	 * dependencies of the newly created instance.
	 *
	 * @param context the injection engine
	 * @param clazz the class
	 * @param <T> the type of the class
	 *
	 * @return an instance of the class with it's properties resolved
	 */
	public <T> T constructClass(final InjectionContext context, final Class<T> clazz) {
		this.logger.debug( "Construct an instance of class {}", clazz.getSimpleName() );

		T classInstance = null;
		ContextBeanStoreImpl beanStore = (ContextBeanStoreImpl) context.getContextBeanStore();
		BindingHolder holder = beanStore.getBindingHolder();
		BindingHolder providerHolder = beanStore.getProviderHolder();

		//Check if bean is a singleton and have been already created
		if ( beanStore.getSingletonBeans().containsKey( clazz ) ) {
			return clazz.cast( beanStore.getSingletonBeans().get( clazz ) );
		}

		//Check if there is a cyclic dependency reference
		if ( context.isAlreadyProcessed( clazz ) ) {
			Object objectRef = context.getResultOfProcessionFor( clazz );
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

					context.markClassAsProcessed( clazz, null );

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
									parameters[i] = new GenericProvider(
											beanStore, injectionBinding.getImplementation()
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
					context.removeMarkFor( clazz );
					break;
				}

			}

			if ( classInstance == null ) {
				classInstance = clazz.newInstance();
			}

		}
		catch ( Exception ex ) {
			throw new BeanInstantiationException(
					String.format(
							"The %s class cannot be instantiated", clazz.getName()
					), ex
			);
		}

		//apply bean post processor on the newly created bean
		for ( BeanProcessor processor : beanProcessors ) {
			if ( processor.isProcessable( classInstance ) ) {
				classInstance = processor.processBean( classInstance );
			}
		}

		//hold singletons and prototypes bean if needed
		if (clazz.isAnnotationPresent( Singleton.class ) || isMethodAnnotatedWith( PreDestroy.class, clazz ) ) {
			beanStore.put( clazz, classInstance );
		}

		//inject fields and method
		injectFieldAndMethod( context, clazz, classInstance );

		//Dependency injection is done call PostContruct method
		Method postConstructMethod = getMethodAnnotatedWith( PostConstruct.class, clazz );
		if ( postConstructMethod != null && isValidCallbackMethod( postConstructMethod ) ) {
			if ( !postConstructMethod.isAccessible() ) {
				postConstructMethod.setAccessible( true );
			}
			invokeMethod( classInstance, postConstructMethod );
		}

		return classInstance;
	}

	/**
	 * Inject dependencies into an existing beans.
	 *
	 * @param context the injection engine
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
	 * @param context the injection engine
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

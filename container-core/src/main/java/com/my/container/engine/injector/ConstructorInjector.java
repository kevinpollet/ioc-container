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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;
import javax.inject.Qualifier;

import com.my.container.BeanDependencyInjectionException;
import com.my.container.BeanInstantiationException;
import com.my.container.InjectionContext;
import com.my.container.NoSuchBeanDefinitionException;
import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.engine.BeanStoreImpl;
import com.my.container.engine.DefaultInstanceProvider;
import com.my.container.spi.BeanProcessor;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	/**
	 * Construct an Injector instance.
	 */
	public ConstructorInjector(List<BeanProcessor> beanProcessors) {
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
	public <T> T constructClass(InjectionContext context, Class<T> clazz) {
		logger.debug( "Construct an instance of class {}", clazz.getSimpleName() );

		T classInstance = null;
		BeanStoreImpl beanStore = (BeanStoreImpl) context.getBeanStore();
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

		Set<Constructor<?>> constructors = ReflectionHelper.getInjectableConstructors( clazz );

		if ( !constructors.isEmpty() ) {  //use first injectable constructor

			Constructor<?> constructor = constructors.iterator().next();
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

				if ( !Provider.class.isAssignableFrom( classToInject ) ) {
					injectionBinding = holder.getBindingFor( classToInject, qualifier );
					if ( injectionBinding == null ) {
						throw new NoSuchBeanDefinitionException( "There is no binding defined for the class " + classToInject.getName() );
					}
					parameters[i] = constructClass( context, injectionBinding.getImplementation() );

				} //it's a user provider injection
				else {
					classToInject = (Class<?>) ( (ParameterizedType) parameterType ).getActualTypeArguments()[0];

					//check if user has defined a custom provider
					injectionBinding = providerHolder.getBindingFor( classToInject, qualifier );
					if ( injectionBinding != null ) {
						parameters[i] = constructClass( context, injectionBinding.getProvider() );
					}
					else {
						injectionBinding = holder.getBindingFor( classToInject, qualifier );
						if ( injectionBinding != null ) {
							parameters[i] = new DefaultInstanceProvider( beanStore, injectionBinding.getImplementation() );
						}
						else {
							throw new NoSuchBeanDefinitionException( "There is no binding defined for the class " + classToInject.getName() );
						}
					}
				}

			}

			try {
				if ( !Modifier.isPrivate( constructor.getModifiers() ) && !constructor.isAccessible() ) {
					constructor.setAccessible( true );
				}

		   		classInstance = clazz.cast( constructor.newInstance( parameters ) );
				context.removeMarkFor( clazz );
			}
			catch ( Exception ex ) {
				throw new BeanInstantiationException("The " + clazz.getName() + " class cannot be instantiated", ex);
			}

		}
		else {
			try {
		   		classInstance = clazz.newInstance();
			}
			catch ( Exception ex ) {
				throw new BeanInstantiationException("The " + clazz.getName() + " class cannot be instantiated", ex);
			}
		}

		//apply bean post processor on the newly created bean
		for ( BeanProcessor processor : beanProcessors ) {
			if ( processor.isProcessable( classInstance ) ) {
				classInstance = processor.processBean( classInstance );
			}
		}

		//inject fields and method
		injectFieldAndMethod( context, clazz, classInstance );

		//hold constructed bean instance
		beanStore.put( classInstance );

		return classInstance;
	}

	/**
	 * Inject fields and method in a bean instance. Fields and Methods
	 * in SuperClass are injected first.
	 *
	 * @param context the injection engine
	 * @param clazz the current class injected
	 * @param instance the instance where values are injected
	 */
	private void injectFieldAndMethod(InjectionContext context, Class<?> clazz, Object instance) {
		Class<?> superClass = clazz.getSuperclass();

		if ( !superClass.equals( Object.class ) ) {
			injectFieldAndMethod( context, superClass, instance );
		}

		FieldInjector.injectFieldsDependencies( context, clazz, instance );
		MethodInjector.injectMethodsDependencies( context, clazz, instance );
	}

}

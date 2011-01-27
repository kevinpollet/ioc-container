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
package com.my.container.util;

import com.my.container.BeanException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * Helper who provides useful to deal with reflection.
 *
 * @author Kevin Pollet
 */
public final class ReflectionHelper {

	/**
	 * This class cannot be instantiated.
	 */
	private ReflectionHelper() {
	}

	/**
	 * This method permits to know if a method is
	 * overridden by subclass.
	 *
	 * @param clazz the child bean class in hierarchy
	 * @param method the method to test
	 *
	 * @return true is the method is overridden in subclass
	 */
	public static boolean isOverridden(Class<?> clazz, Method method) {
		if ( clazz == null ) {
			throw new NullPointerException( "The clazz parameter cannot be null" );
		}
		if ( method == null ) {
			throw new NullPointerException( "The method parameter cannot be null" );
		}

		String classPackage = clazz.getPackage().getName();
		String declaringClassPackage = method.getDeclaringClass().getPackage().getName();

		//A private method cannot be overridden
		if ( clazz.equals( method.getDeclaringClass() ) || Modifier.isPrivate( method.getModifiers() ) ) {
			return false;
		}

		try {

			clazz.getDeclaredMethod( method.getName(), method.getParameterTypes() );

			//A package private method can only be overridden in the same package
			if ( method.getModifiers() != 0 || classPackage.equals( declaringClassPackage ) ) {
				return true;
			}

		}
		catch ( NoSuchMethodException e ) {
			//Ignore - try to find it in the class hierarchy
		}
		return isOverridden( clazz.getSuperclass(), method );
	}

	/**
	 * Check if the given class have a declared method annotated with
	 * the given annotation.
	 *
	 * @param annotation the annotation
	 * @param clazz the class
	 *
	 * @return true if there is a corresponding method.
	 */
	public static boolean isMethodAnnotatedWith(Class<? extends Annotation> annotation, Class<?> clazz) {
		return getMethodAnnotatedWith( annotation, clazz ) != null;
	}

	/**
	 * Get the method declared in the class who is annotated by the given annotation.
	 *
	 * @param annotation the annotation class
	 * @param clazz the class
	 *
	 * @return the method or null if none
	 */
	public static Method getMethodAnnotatedWith(Class<? extends Annotation> annotation, Class<?> clazz) {
		Method annotatedMethod = null;

		for ( Method method : clazz.getDeclaredMethods() ) {
			if ( method.isAnnotationPresent( annotation ) ) {
				annotatedMethod = method;
				break;
			}
		}
		return annotatedMethod;
	}

	/**
	 * Invoke the given method on the given object with the given args.
	 *
	 * @param object the object to call method on
	 * @param method the method to call
	 * @param args the method args
	 *
	 * @return the result of the invocation
	 * @throws BeanException if invocation of method failed
	 */
	public static Object invokeMethod(Object object, Method method, Object... args) {
		try {
			return method.invoke( object, args );
		}
		catch ( InvocationTargetException e ) {
			throw new BeanException(
					String.format(
							"The invocation of method %s on class %s failed",
							method.getName(),
							object.getClass().getName()
					), e
			);
		}
		catch ( IllegalAccessException e ) {
			throw new BeanException(
					String.format(
							"The invocation of method %s on class %s failed due to illegal access",
							method.getName(),
							object.getClass().getName()
					), e
			);
		}
	}

}

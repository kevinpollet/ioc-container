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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class provides validation methods.
 *
 * @author Kevin Pollet
 */
public final class ValidationHelper {

	/**
	 * Don't instantiate this helper.
	 */
	private ValidationHelper() {

	}

	/**
	 * Validate that the given method is a valid callback
	 * method (JSR 250 definition of callback method).
	 * A valid callback method must fulfill all this criteria :
	 * <ul>
	 * <li>Must have any parameters</li>
	 * <li>Must return type must be void</li>
	 * <li>Must Not throw a checked exception<li>
	 * <li>MAY be final or non-final</li>
	 * <li>The method on which PostConstruct is applied MAY be public, protected, package private or private.</li>
	 * </ul>
	 *
	 * @param method the method to verify
	 *
	 * @return true if the given method is a valid callback.
	 */
	public static boolean isValidCallbackMethod(Method method) {

		if ( !method.getReturnType().equals( Void.TYPE ) ||
			  method.getParameterTypes().length != 0 ||
			  Modifier.isStatic( method.getModifiers() ) ) {

			return false;
		}

		Class<?>[] exceptionsClass = method.getExceptionTypes();
		for (Class<?> exceptionClass : exceptionsClass) {
			if ( Exception.class.isAssignableFrom( exceptionClass ) ) {
				return false;
			}
		}

		return true;
	}


}

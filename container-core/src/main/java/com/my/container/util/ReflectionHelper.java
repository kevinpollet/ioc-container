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
package com.my.container.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper who provides useful
 * methods for reflection.
 *
 * @author kevinpollet
 */
public final class ReflectionHelper {

    /**
     * This class cannot be instantiated.
     */
    private ReflectionHelper() {
    }

    /**
     * Check if there is callback method in the
     * given clazz. A callback method is a method
     * annotated by {link PostConstruct} or {link PreDestroy}.
     *
     * @param clazz the clazz
     * @return true if one, false otherwise
     * @throws IllegalArgumentException if clazz parameter is null
     */
    public static boolean isCallbackMethod(final Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        }

        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class) ||
                method.isAnnotationPresent(PreDestroy.class)) {

                return true;
            }
        }

        return false;
    }
    
    /**
     * This method permits to know if a method is
     * overridden by subclass.
     *
     * @param clazz the child bean class in hierarchy
     * @param method the method to test
     * @return true is the method is overridden in subclass
     */
    public static boolean isOverridden(final Class<?> clazz, final Method method) {
        if (method == null) {
            throw new IllegalArgumentException("The method parameter cannot be null");
        }

        Class<?> currentClass = clazz;
        Class<?> declaringClass = method.getDeclaringClass();

        while (currentClass != null && !currentClass.equals(declaringClass)) {
            try {
                currentClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return true;
            }
            catch (NoSuchMethodException e) {
                //Test if method exist in class hierarchy
                currentClass = currentClass.getSuperclass();
            }
        }
        return false;
    }

    /**
     * Get all methods declared in the class
     * who are annotated by this annotation.
     *
     * @param annotation the annotation class
     * @param clazz      the class
     * @return the list of method
     * @see Class#getDeclaredMethods()
     */
    public static List<Method> getDeclaredMethodsAnnotatedWith(final Class<? extends Annotation> annotation, final Class<?> clazz) {
        List<Method> methodList = new ArrayList<Method>();
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(annotation)) {
                methodList.add(m);
            }
        }

        return methodList;
    }

    /**
     * Call the first declared method with the given
     * annotation. This method called only the declared
     * public, private and protected method in the given
     * class.
     *
     * @param annotation the annotation
     * @param instance   the instance to call on
     * @param params     the method parameter
     * @return the method result
     */
    public static Object invokeDeclaredMethodWith(final Class<? extends Annotation> annotation, final Object instance, final Object... params) throws InvocationTargetException, IllegalAccessException {
        Object result = null;

        for (Method m : instance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(annotation)) {
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                result = m.invoke(instance, params);
                break;
            }
        }

        return result;
    }

}

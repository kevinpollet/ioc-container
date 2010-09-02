package com.my.container.util;

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
//TODO gestion des exceptions
public final class ReflectionHelper {

    /**
     * This class cannot be instantiated.
     */
    private ReflectionHelper() {
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

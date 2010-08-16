package com.my.container.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * A class who provides useful
 * methods for reflection.
 *
 * @author kevinpollet
 */
public class ReflectionHelper {

    /**
     * Get all methods declared in the class
     * who have this annotation.
     *
     * @param annotation the annotation class
     * @param clazz      the class
     * @return the list of method
     */
    public static List<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation, final Class<?> clazz) {
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
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object callDeclaredMethodWith(final Class<? extends Annotation> annotation, final Object instance, final Object... params) throws InvocationTargetException, IllegalAccessException {

        Object result = null;

        for (Method m : instance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(annotation)) {

                //Check if the method is private
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

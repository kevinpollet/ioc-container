package com.my.container.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReflectionHelper {

    public static Object callDeclaredMethodWith(final Class<? extends Annotation> annotation, final Object instance) throws InvocationTargetException, IllegalAccessException {

        Object result = null;

        for (Method m : instance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(annotation)) {

                //Check if the method is private
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }

                result = m.invoke(instance, null);
                break;
            }
        }

        return result;
    }

}

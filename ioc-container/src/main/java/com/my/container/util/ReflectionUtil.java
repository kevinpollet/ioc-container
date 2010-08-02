package com.my.container.util;

import com.my.container.annotations.After;
import com.my.container.annotations.Around;
import com.my.container.annotations.Interceptors;
import com.my.container.annotations.Listeners;
import com.my.container.annotations.Before;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ReflectionUtil {

    /**
     * Get all declared method who have this specifics
     * annotation.
     *
     * @param clazz the class
     * @param annotation the annotation
     * @return the method list
     */
    public static List<Method> getMethodAnnotatedWith(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        List<Method> methods =  new ArrayList<Method>();
        Method[] classMethods = clazz.getDeclaredMethods();

        for (Method m : classMethods) {
            if (m.isAnnotationPresent(annotation)) {
                methods.add(m);
            }
        }

        return methods;
    }

}

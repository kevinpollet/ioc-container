package com.my.container.annotations.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The Before annotation.
 * This injectservice method can match this signature :
 * void methodName (Object instance, Method method, Object... args)
 *
 * @author kevinpollet
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
}

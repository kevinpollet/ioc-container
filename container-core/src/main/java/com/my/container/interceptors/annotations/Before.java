package com.my.container.interceptors.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>The before interceptor annotation. The annotation must be used on a method.
 * The annotated method must have this signature :
 * {@code void methodName (Object instance, Method method, Object... args)}</p>
 *
 * @author kevinpollet
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
}

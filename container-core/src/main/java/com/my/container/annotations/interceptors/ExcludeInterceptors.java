package com.my.container.annotations.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The exclude interceptors annotation.
 * When a method is annotated by this annotation
 * the injectservice is not applied to it.
 *
 * @author kevinpollet
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcludeInterceptors {
}

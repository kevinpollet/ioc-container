package com.my.container.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation is used to annotate at most
 * one method in a Pojo (manage by the container).
 * This method was called when a public method is
 * called in the Pojo. Only one anotated method must
 * </p>
 * 
 * @author kevinpollet
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AroundInvoke { 
}

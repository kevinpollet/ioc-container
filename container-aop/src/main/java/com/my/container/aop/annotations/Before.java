package com.my.container.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * The Before interceptor annotation.
 * </p>
 * <p>
 * This annotation must be used on a method with the signature :
 * <ul><li>void methodName (final JointPoint jp)</li></ul>
 * </p>
 *
 * @author kevinpollet
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
}

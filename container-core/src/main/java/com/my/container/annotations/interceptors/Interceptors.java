package com.my.container.annotations.interceptors;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>The Interceptor annotation. This annotation allow to declare
 * an array of interceptor class. This annotation can have
 * {@linkplain com.my.container.annotations.interceptors.Before @Before} and/or
 * {@linkplain com.my.container.annotations.interceptors.After @After} annotated methods.</p>
 * <br/>
 * <p><b>Note :</b> an interceptor must have only one @After and/or one @Before annotated method.</p>
 *
 * @author kevinpollet
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptors {

    public Class<?>[] value();
}

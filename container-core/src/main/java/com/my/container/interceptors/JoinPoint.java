package com.my.container.interceptors;

import java.lang.reflect.Method;

/**
 * <p>
 * This class define the JoinPoint parameter class
 * of interceptors methods annotated with
 * {@linkplain com.my.container.interceptors.annotations.Before @Before}
 * and {@linkplain com.my.container.interceptors.annotations.After @After}.
 * </p>
 *
 * @author kevinpollet
 */
public class JoinPoint {

    private Object target;
    private Method method;
    private Object[] parameters;

    public JoinPoint(final Object target, final Method method, final Object[] parameters) {
        this.parameters = parameters;
        this.method = method;
        this.target = target;
    }
    
    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

}

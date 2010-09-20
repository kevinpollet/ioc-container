package com.my.container.interceptors;

import java.lang.reflect.Method;

/**
 * This class define the JoinPoint parameter class
 * of interceptors methods annotated with
 * {@linkplain com.my.container.interceptors.annotations.AroundInvoke @AroundInvoke}.
 *
 * @author kevinpollet
 */
public class ProceedingJoinPoint extends JoinPoint {

    public ProceedingJoinPoint(Object target, Method method, Object[] args) {
        super(target, method, args);
    }

    public Object proceed() throws Exception {
        return this.getMethod().invoke(this.getTarget(), this.getParameters());
    }
}

package com.my.container.aop.invocation;

import java.lang.reflect.Method;

/**
 * <p>
 * This class define the ProceedingJoinPoint parameter class
 * of interceptors methods annotated with
 * {@linkplain com.my.container.aop.annotations.AroundInvoke @AroundInvoke}.
 * </p>
 * <p>
 * The ProceedingJoinPoint class give to interceptor
 * the context of the invocation and the possibilities to 
 * invoke the method intercepted.
 * </p>
 *
 * @author kevinpollet
 */
public class ProceedingJoinPoint extends JoinPoint {

    /**
     * The ProceedingJoinPoint class.
     *  
     * @param target the target object
     * @param method the method being called
     * @param parameters the method parameters
     */
    public ProceedingJoinPoint(final Object target, final Method method, final Object[] parameters) {
        super(target, method, parameters);
    }

    /**
     * Invoke the method intercepted.
     *
     * @return the method results
     * @throws Exception the method exception
     */
    public Object proceed() throws Exception {
        return this.getMethod().invoke(this.getTarget(), this.getParameters());
    }
}

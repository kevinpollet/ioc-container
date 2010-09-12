package com.my.container.test.interceptors.services;

import com.my.container.interceptors.ProceedingJoinPoint;
import com.my.container.interceptors.annotations.AroundInvoke;

/**
 * @author kevinpollet
 */
public class AroundInvokeServiceImpl implements Service {

    private int beforeCall = 0;

    private int afterCall = 0;

    @Override
    public String sayHello() {
        return "Hello";
    }

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @AroundInvoke
    private Object aroundInvoke(final ProceedingJoinPoint joinPoint) throws Exception{

        this.beforeCall++;

        Object result = joinPoint.proceed();
        
        this.afterCall++;

        return result;
    }

    public int getBeforeCall() {
        return this.beforeCall;
    }

    public int getAfterCall() {
        return this.afterCall;
    }

}

package com.my.container.test.interceptors.services;

import com.my.container.annotations.interceptors.ExcludeInterceptors;
import com.my.container.annotations.interceptors.Interceptors;

/**
 * A service with interceptors.
 * 
 * @author kevinpollet
 */
@Interceptors({MockInterceptor.class})
public class InterceptorServiceImpl implements Service {

    private final String text;

    public InterceptorServiceImpl() {
        this.text = "Hello";
    }

    public String sayHello() {
        return this.text;
    }

    @ExcludeInterceptors
    public String sayHello(final String name) {
        return this.text + " " + name;
    }
}

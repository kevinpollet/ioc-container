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

    public String sayHello() {
        return "Hello";
    }

    @ExcludeInterceptors
    public String sayHello(final String name) {
        return this.sayHello() + " " + name;
    }
}

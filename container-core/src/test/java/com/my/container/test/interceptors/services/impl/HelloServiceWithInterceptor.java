package com.my.container.test.interceptors.services.impl;

import com.my.container.interceptors.annotations.ExcludeInterceptors;
import com.my.container.interceptors.annotations.Interceptors;
import com.my.container.test.interceptors.services.HelloService;

/**
 * A service with interceptors.
 * 
 * @author kevinpollet
 */
@Interceptors({MockInterceptor.class})
public class HelloServiceWithInterceptor implements HelloService {

    public String sayHello() {
        return "Hello";
    }

    @ExcludeInterceptors
    public String sayHello(final String name) {
        return this.sayHello() + " " + name;
    }
}

package com.my.container.aop.services.impl;

import com.my.container.aop.annotations.ExcludeInterceptors;
import com.my.container.aop.annotations.Interceptors;
import com.my.container.aop.services.HelloService;

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

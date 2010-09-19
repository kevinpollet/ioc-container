package com.my.container.test.scope.services.impl;


import com.my.container.test.scope.services.HelloService;

import javax.inject.Singleton;


/**
 * The Singleton scoped service
 * implementation.
 *
 * @author kevinpollet
 */
@Singleton
public class SingletonHelloService implements HelloService {

    public String sayHello() {
        return "Hello";
    }

}

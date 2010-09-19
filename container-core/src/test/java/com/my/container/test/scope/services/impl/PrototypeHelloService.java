package com.my.container.test.scope.services.impl;

import com.my.container.test.scope.services.HelloService;

/**
 * The prototype scoped service
 * implementation.
 * 
 * @author kevinpollet
 */
public class PrototypeHelloService implements HelloService {

    public String sayHello() {
        return "Hello";
    }

}

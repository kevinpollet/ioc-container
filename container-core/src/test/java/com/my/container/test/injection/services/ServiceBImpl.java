package com.my.container.test.injection.services;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ServiceBImpl implements ServiceB {

    @Inject
    private ServiceA serviceA;

    @Override
    public String sayHello() {
        return "Hello";
    }
}

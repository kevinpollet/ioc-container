package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;

import javax.inject.Inject;

/**
 * Implementation of the Service B.
 *
 * @author kevinpollet
 */
public class ConstructorServiceBImpl implements ServiceB {

    private ServiceA serviceA;

    @Inject
    public ConstructorServiceBImpl(final ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public String sayHello() {
        return "Hello";
    }
}

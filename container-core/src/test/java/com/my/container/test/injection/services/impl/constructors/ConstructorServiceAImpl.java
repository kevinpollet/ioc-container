package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;

import javax.inject.Inject;

/**
 * Implementation of the Service A.
 *
 * @author kevinpollet
 */
public class ConstructorServiceAImpl implements ServiceA {

    private ServiceB serviceB;

    @Inject
    public ConstructorServiceAImpl(final ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String echo(final String string) {
        return string;
    }

    public String sayHelloTo(final String name) {
        return "Hello " + name;
    }
}

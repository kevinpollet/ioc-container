package com.my.container.test.injection.services.impl.fields;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class FieldServiceBImpl implements ServiceB {

    @Inject
    private ServiceA serviceA;

    public String sayHello() {
        return "Hello";
    }
}

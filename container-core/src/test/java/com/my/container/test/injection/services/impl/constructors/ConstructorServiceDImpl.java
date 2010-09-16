package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Implementation of the Service D.
 *
 * @author kevinpollet
 */
public class ConstructorServiceDImpl implements ServiceD {

    private final ServiceC servicec;

    @Inject
    public ConstructorServiceDImpl(final ServiceC servicec) {
        this.servicec = servicec;
    }

    @Override
    public String echo(final String echo) {
        return servicec.echo(echo);
    }

    @Override
    public int add(final int a, final int b) {
        return a+b;
    }
}

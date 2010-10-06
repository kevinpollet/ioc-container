package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceC;

import javax.inject.Inject;

/**
 * Implementation of the Service C.
 *
 * @author kevinpollet
 */
public class ConstructorServiceCImpl implements ServiceC {

    @Inject
    public ConstructorServiceCImpl() {

    }

    public String echo(final String echo) {
        return echo;
    }
}

package com.my.container.test.injection.services;


import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ServiceAImpl extends AbstractService implements ServiceA {

    @Inject
    private ServiceB serviceB;

    @Override
    public String sayHelloTo(final String name) {
        return this.serviceB.sayHello() + " " + this.getServiceC().echo(name);
    }
}

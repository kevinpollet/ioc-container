package com.my.container.test.injection.services.impl.fields;


import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class FieldServiceAImpl extends FieldAbstractService implements ServiceA {

    @Inject
    private ServiceB serviceB;

    @Inject
    private ServiceC serviceC;

    @Override
    public String sayHelloTo(final String name) {
        return this.serviceB.sayHello() + " " + this.getServiceC().echo(name);
    }

    @Override
    public String echo(final String string) {
        return this.serviceC.echo(string);
    }
}

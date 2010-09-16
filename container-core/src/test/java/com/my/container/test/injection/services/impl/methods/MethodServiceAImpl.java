package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceC;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author kevinpollet
 */
public class MethodServiceAImpl implements ServiceA {

    private ServiceC serviceC;

    @Override
    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    @Override
    public String sayHelloTo(final String name) {
        return "Hello " + name;
    }

    @Inject
    public void setServiceC(@Named("upperEchoService") final ServiceC serviceC) {
        this.serviceC = serviceC;
    }
}

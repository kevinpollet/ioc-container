package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceC;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author kevinpollet
 */
public class MethodNamedServiceA implements ServiceA {

    private ServiceC serviceC;

    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    public String sayHelloTo(final String name) {
        return "Hello " + name;
    }

    @Inject
    public void setServiceC(@Named("upperEchoService") final ServiceC serviceC) {
        this.serviceC = serviceC;
    }
}

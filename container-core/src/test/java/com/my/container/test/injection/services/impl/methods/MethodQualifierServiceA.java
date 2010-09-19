package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.impl.LowerEcho;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class MethodQualifierServiceA implements ServiceA {

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
    public void setServiceC(@LowerEcho final ServiceC serviceC) {
        this.serviceC = serviceC;
    }
}

package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceE;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author kevinpollet
 */
public class ConstructorQualifierServiceE implements ServiceE {

    public ServiceC serviceC;

    @Inject
    public ConstructorQualifierServiceE(@Named("upperEchoService") final ServiceC serviceC) {
        this.serviceC = serviceC;
    }

    @Override
    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    @Override
    public int add(final int a, final int b) {
        return a+b;
    }
}

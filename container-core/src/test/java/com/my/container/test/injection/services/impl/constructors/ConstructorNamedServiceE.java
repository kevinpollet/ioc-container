package com.my.container.test.injection.services.impl.constructors;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceE;
import com.my.container.test.injection.services.impl.LowerEcho;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ConstructorNamedServiceE implements ServiceE {

    public ServiceC serviceC;

    @Inject
    public ConstructorNamedServiceE(@LowerEcho final ServiceC serviceC) {
        this.serviceC = serviceC;
    }

    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    public int add(final int a, final int b) {
        return a + b;
    }
}

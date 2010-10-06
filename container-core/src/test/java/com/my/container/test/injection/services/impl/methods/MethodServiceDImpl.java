package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class MethodServiceDImpl extends MethodOverrideService implements ServiceD {

    private ServiceC serviceC;

    private ServiceE serviceE;

    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    public int add(int a, int b) {
        return this.serviceE.add(a, b);
    }

    @Inject
    @Override
    public void setServiceC(final ServiceC service) {
        this.nbCallSetServiceC++;
        this.serviceC = service;
    }


    @Inject
    public void setServiceE(final ServiceE service) {
        this.serviceE = service;
    }

}

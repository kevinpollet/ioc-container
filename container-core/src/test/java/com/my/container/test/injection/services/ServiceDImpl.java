package com.my.container.test.injection.services;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author kevinpollet
 */
public class ServiceDImpl extends OverrideMethodService implements ServiceD {

    private ServiceC serviceC;

    private ServiceE serviceE;

    @Override
    public String echo(final String string) {
        return this.serviceC.echo(string);
    }

    @Override
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

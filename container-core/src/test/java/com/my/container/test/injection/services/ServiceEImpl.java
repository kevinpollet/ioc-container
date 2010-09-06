package com.my.container.test.injection.services;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ServiceEImpl extends OverrideMethodService implements ServiceE {

    private ServiceD serviceD;

    private ServiceC serviceC;

    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public String echo(final String string) {
        return this.serviceD.echo(string);
    }

    @Override
    public void setServiceC(final ServiceC serviceC) {
         this.serviceC = serviceC;
    }

    @Inject
    private void setServiceD(final ServiceD service) {
        this.serviceD = service;
    }
}

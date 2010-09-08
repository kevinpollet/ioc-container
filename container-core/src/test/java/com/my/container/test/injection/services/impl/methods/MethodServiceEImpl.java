package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class MethodServiceEImpl extends MethodOverrideService implements ServiceE {

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

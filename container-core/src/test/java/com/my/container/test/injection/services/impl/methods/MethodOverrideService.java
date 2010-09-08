package com.my.container.test.injection.services.impl.methods;

import com.my.container.test.injection.services.ServiceC;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public abstract class MethodOverrideService {

    protected int nbCallSetServiceC = 0;

    @Inject
    public void setServiceC(final ServiceC serviceC){
        this.nbCallSetServiceC++;
    }


    public int getNbCallSetServiceC() {
        return this.nbCallSetServiceC;
    }
        

}

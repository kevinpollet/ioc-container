package com.my.container.test.injection.services;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class OverrideMethodService {

    protected int nbCallSetServiceC = 0;

    @Inject
    public void setServiceC(final ServiceC serviceC){
        this.nbCallSetServiceC++;
    }


    public int getNbCallSetServiceC() {
        return this.nbCallSetServiceC;
    }
        

}

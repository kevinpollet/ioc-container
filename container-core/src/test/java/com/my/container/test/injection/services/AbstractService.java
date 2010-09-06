package com.my.container.test.injection.services;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public abstract class AbstractService {

    @Inject
    private ServiceC serviceC;

    public ServiceC getServiceC() {
        return this.serviceC;
    }
    
}

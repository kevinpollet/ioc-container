package com.my.container.test.injection.services.impl.fields;

import com.my.container.test.injection.services.ServiceC;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public abstract class FieldAbstractService {

    @Inject
    private ServiceC serviceC;

    public ServiceC getServiceC() {
        return this.serviceC;
    }

}

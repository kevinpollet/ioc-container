package com.my.container.services.injectservice;

import com.my.container.services.basic.Service;

import javax.inject.Inject;

/**
 * @author kevinpollet
 *         Date: 18 août 2010
 */
public class AbstractService {

    @Inject
    private Service service;


    public Service getService() {
        return this.service;
    }


}

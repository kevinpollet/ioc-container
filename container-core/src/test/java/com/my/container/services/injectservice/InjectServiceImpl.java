package com.my.container.services.injectservice;

import com.my.container.services.basic.Service;

import javax.inject.Inject;

/**
 * @author kevinpollet
 *         Date: 17 août 2010
 */
public class InjectServiceImpl extends AbstractService implements InjectService {

    @Inject
    private Service dependency;

    @Override
    public String sayHello() {
        return dependency.sayHello();  
    }

    @Override
    public String sayHello(String name) {
        return super.getService().sayHello(name);
    }
}

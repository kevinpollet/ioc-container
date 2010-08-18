package com.my.container.services.injectservice;

import javax.inject.Inject;

/**
 * @author kevinpollet
 *         Date: 17 août 2010
 */
public interface InjectService {

    public String sayHello();

    public String sayHello(final String name);

}

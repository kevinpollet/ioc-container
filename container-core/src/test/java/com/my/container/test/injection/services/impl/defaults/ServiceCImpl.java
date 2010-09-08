package com.my.container.test.injection.services.impl.defaults;

import com.my.container.test.injection.services.ServiceC;

/**
 * @author kevinpollet
 */
public class ServiceCImpl implements ServiceC {

    @Override
    public String echo(final String echo) {
        return echo;
    }
}

package com.my.container.test.injection.services;

import javax.inject.Inject;

/**
 * @author kevinpollet
 */
public class ServiceCImpl implements ServiceC {

    @Override
    public String echo(final String echo) {
        return echo;
    }
}

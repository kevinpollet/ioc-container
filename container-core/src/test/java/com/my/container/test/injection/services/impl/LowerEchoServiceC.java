package com.my.container.test.injection.services.impl;

import com.my.container.test.injection.services.ServiceC;

/**
 * @author kevinpollet
 */
public class LowerEchoServiceC implements ServiceC {

    @Override
    public String echo(final String echo) {
        return echo.toLowerCase();
    }
}
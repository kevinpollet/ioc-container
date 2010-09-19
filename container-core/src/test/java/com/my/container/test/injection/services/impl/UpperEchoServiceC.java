package com.my.container.test.injection.services.impl;

import com.my.container.test.injection.services.ServiceC;

/**
 * @author kevinpollet
 */
public class UpperEchoServiceC implements ServiceC {

    @Override
    public String echo(String echo) {
        return echo.toUpperCase();
    }
}

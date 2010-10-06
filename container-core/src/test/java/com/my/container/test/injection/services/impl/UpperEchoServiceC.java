package com.my.container.test.injection.services.impl;

import com.my.container.test.injection.services.ServiceC;

/**
 * @author kevinpollet
 */
public class UpperEchoServiceC implements ServiceC {

    public String echo(String echo) {
        return echo.toUpperCase();
    }
}

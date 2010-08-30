package com.my.container.test.interceptors.services;

/**
 * The basic service contract.
 */
public interface Service {

    public String sayHello();

    public String sayHello(final String name);
}

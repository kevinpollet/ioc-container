package com.my.container.test.interceptors.services;

/**
 * The basic service contract.
 */
public interface HelloService {

    public String sayHello();

    public String sayHello(final String name);
}

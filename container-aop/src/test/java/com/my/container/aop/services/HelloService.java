package com.my.container.aop.services;

/**
 * The basic service contract.
 */
public interface HelloService {

    public String sayHello();

    public String sayHello(final String name);
}

package com.my.container.test.scope.services;


import javax.inject.Singleton;


/**
 * A singleton service.
 */
@Singleton
public class SingletonServiceImpl implements Service {

    private final String text;

    public SingletonServiceImpl() {
        this.text = "Hello";
    }

    public String sayHello() {
        return this.text;
    }

    public String sayHello(final String name) {
        return this.text + " " + name;
    }

}

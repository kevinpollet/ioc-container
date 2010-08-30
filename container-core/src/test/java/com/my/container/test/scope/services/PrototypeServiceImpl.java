package com.my.container.test.scope.services;

/**
 * Protoype service.
 * 
 * @author kevinpollet
 */
public class PrototypeServiceImpl implements Service {

    private final String text;

    public PrototypeServiceImpl() {
        this.text = "Hello";
    }

    public String sayHello() {
        return this.text;
    }

    public String sayHello(final String name) {
        return this.text + " " + name;
    }

}

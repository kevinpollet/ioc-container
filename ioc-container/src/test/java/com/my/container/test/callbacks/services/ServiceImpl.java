package com.my.container.test.callbacks.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;


@Singleton
public class ServiceImpl implements Service {

    private String text;


    @PostConstruct
    private void init() {
        this.text = "Hello";
    }

    public String sayHello() {
        return this.text;
    }

    @PreDestroy
    private void destroy() {
        
    }
}

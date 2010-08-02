package com.my.container.services;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;


@Singleton
public class ServiceImpl implements Service {

    private String text;

    @PostConstruct
    public void init() {
        this.text = "Hello";
    }

    public String sayHello() {
        return this.text;
    }
    
}

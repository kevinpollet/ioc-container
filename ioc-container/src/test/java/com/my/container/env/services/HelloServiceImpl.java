package com.my.container.env.services;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;


@Singleton
public class HelloServiceImpl implements HelloService {

    private String text;


    @PostConstruct
    private void init() {
        this.text = "HelloConstruct";
    }

    public String sayHello() {
        return this.text;
    }

 
}

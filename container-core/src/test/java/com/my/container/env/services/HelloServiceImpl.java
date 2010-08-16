package com.my.container.env.services;

import com.my.container.annotations.interceptors.Interceptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;


@Singleton
@Interceptors(HelloInterceptor.class)
public class HelloServiceImpl implements HelloService {

    private final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    private String text;

    @PostConstruct
    private void init() {
        this.logger.debug("Called the @PostConstruct method");
        this.text = "HelloConstruct";
    }

    public String sayHello() {
        this.logger.debug("Called the sayHello service method");
        return this.text;
    }

    @PreDestroy
    private void destroy() {
        this.logger.debug("Called the @PreDestroy method");
        this.text = "HelloDestroy";
    }
}

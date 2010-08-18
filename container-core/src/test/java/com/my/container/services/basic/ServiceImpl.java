package com.my.container.services.basic;

import com.my.container.annotations.interceptors.ExcludeInterceptors;
import com.my.container.annotations.interceptors.Interceptors;
import com.my.container.services.MockInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;


@Singleton
@Interceptors(MockInterceptor.class)
public class ServiceImpl implements Service {

    private final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);
    private String text;

    @PostConstruct
    private void init() {
        this.logger.debug("Called the @PostConstruct method");
        this.text = "HelloConstruct";
    }

    @Override
    public String sayHello() {
        this.logger.debug("Called the sayHello service method");
        return this.text;
    }

    @Override
    @ExcludeInterceptors
    public String sayHello(final String name) {
        this.logger.debug("Called the sayHello service method with parameter");
        return this.text + name;
    }

    @PreDestroy
    private void destroy() {
        this.logger.debug("Called the @PreDestroy method");
        this.text = "HelloDestroy";
    }
}

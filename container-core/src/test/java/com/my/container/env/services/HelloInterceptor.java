package com.my.container.env.services;

import com.my.container.annotations.interceptors.After;
import com.my.container.annotations.interceptors.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class HelloInterceptor {

    private final Logger logger = LoggerFactory.getLogger(HelloInterceptor.class);

    @Before
    public void before(Object instance, Method method, Object[] args) {
        this.logger.debug("Before : {} on method named {}", new Object[]{instance,method.getName()});
    }

    @After
    public void after(Object instance, Method method, Object[] args) {
        this.logger.debug("After : {} on method named {}", new Object[]{instance,method.getName()});
    }
}

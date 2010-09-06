package com.my.container.test.interceptors.services;

import com.my.container.annotations.interceptors.After;
import com.my.container.annotations.interceptors.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class MockInterceptor {

    private final Logger logger = LoggerFactory.getLogger(MockInterceptor.class);

    private int beforeNbCall;
    
    private int afterNbCall;

    public MockInterceptor() {
        this.beforeNbCall = 0;
        this.afterNbCall = 0;
    }

    @Before
    public void before(Object instance, Method method, Object[] args) {
        this.logger.debug("Before : {} on method named {}", instance, method.getName());
        this.beforeNbCall++;
    }

    @After
    public void after(Object instance, Method method, Object[] args) {
        this.logger.debug("After : {} on method named {}", instance, method.getName());
        this.afterNbCall++;
    }

    /**
     * Get the number
     * @return the number of before aspect call
     */
    public int getBeforeNbCall() {
        return this.beforeNbCall;
    }

    public int getAfterNbCall() {
        return this.afterNbCall;
    }
}

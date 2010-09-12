package com.my.container.test.interceptors.services;

import com.my.container.interceptors.JoinPoint;
import com.my.container.interceptors.annotations.After;
import com.my.container.interceptors.annotations.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockInterceptor {

    private final Logger logger = LoggerFactory.getLogger(MockInterceptor.class);

    private int beforeNbCall;
    
    private int afterNbCall;

    public MockInterceptor() {
        this.beforeNbCall = 0;
        this.afterNbCall = 0;
    }

    @Before
    public void before(final JoinPoint jointPoint) {
        this.logger.debug("Before : {} on method named {}", jointPoint.getTarget(), jointPoint.getMethod().getName());
        this.beforeNbCall++;
    }

    @After
    public void after(final JoinPoint jointPoint) {
        this.logger.debug("After : {} on method named {}", jointPoint.getTarget(), jointPoint.getMethod().getName());
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

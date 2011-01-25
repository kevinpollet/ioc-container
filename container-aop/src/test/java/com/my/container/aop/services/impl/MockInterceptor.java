/*
 * Copyright 2011 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.aop.services.impl;

import com.my.container.aop.invocation.JoinPoint;
import com.my.container.aop.annotations.After;
import com.my.container.aop.annotations.Before;
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

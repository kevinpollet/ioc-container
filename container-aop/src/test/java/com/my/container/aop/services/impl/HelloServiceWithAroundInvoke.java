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

import com.my.container.aop.invocation.ProceedingJoinPoint;
import com.my.container.aop.annotations.AroundInvoke;
import com.my.container.aop.services.HelloService;

/**
 * The Hello service implementation
 * with an @AroundInvoke method.
 * 
 * @author kevinpollet
 */
public class HelloServiceWithAroundInvoke implements HelloService {

    private int beforeCall = 0;

    private int afterCall = 0;

    public String sayHello() {
        return "Hello";
    }

    public String sayHello(String name) {
        return "Hello " + name;
    }

    @AroundInvoke
    private Object aroundInvoke(final ProceedingJoinPoint joinPoint) throws Exception{

        this.beforeCall++;

        Object result = joinPoint.proceed();
        
        this.afterCall++;

        return result;
    }

    public int getBeforeCall() {
        return this.beforeCall;
    }

    public int getAfterCall() {
        return this.afterCall;
    }

}

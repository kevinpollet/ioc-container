/*
 * Copyright 2010 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.aop.invocation;

import java.lang.reflect.Method;

/**
 * <p>
 * This class define the JoinPoint parameter class
 * of interceptors methods annotated with {@linkplain com.my.container.aop.annotations.Before @Before}
 * and {@linkplain com.my.container.aop.annotations.After @After}.
 * </p>
 * <p>
 * The JoinPoint class give to interceptor
 * the core of the invocation.
 * </p>
 *
 * @author kevinpollet
 */
public class JoinPoint {

    /**
     * The target object.
     */
    private final Object target;

    /**
     * The method being called.
     */
    private final Method method;

    /**
     * The method invocation parameters.
     */
    private final Object[] parameters;

    /**
     * The JoinPoint constructor.
     *
     * @param target the target object
     * @param method the method being called
     * @param parameters the method parameters
     */
    public JoinPoint(final Object target, final Method method, final Object[] parameters) {
        this.parameters = parameters;
        this.method = method;
        this.target = target;
    }

    /**
     * Get the target object.
     *
     * @return the target object.
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Get the method being called.
     *
     * @return the method
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Get the method invocation parameters.
     *
     * @return the parameters or null if none
     */
    public Object[] getParameters() {
        return this.parameters;
    }

}

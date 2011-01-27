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
package com.my.container.aop.invocation;

import java.lang.reflect.Method;

/**
 * <p>
 * This class define the ProceedingJoinPoint parameter class
 * of interceptors methods annotated with
 * {@linkplain com.my.container.aop.annotations.AroundInvoke @AroundInvoke}.
 * </p>
 * <p>
 * The ProceedingJoinPoint class give to interceptor
 * the engine of the invocation and the possibilities to
 * invoke the method intercepted.
 * </p>
 *
 * @author kevinpollet
 */
public class ProceedingJoinPoint extends JoinPoint {

    /**
     * The ProceedingJoinPoint class.
     *  
     * @param target the target object
     * @param method the method being called
     * @param parameters the method parameters
     */
    public ProceedingJoinPoint(final Object target, final Method method, final Object[] parameters) {
        super(target, method, parameters);
    }

    /**
     * Invoke the method intercepted.
     *
     * @return the method results
     * @throws Exception the method exception
     */
    public Object proceed() throws Exception {
        return this.getMethod().invoke(this.getTarget(), this.getParameters());
    }
}

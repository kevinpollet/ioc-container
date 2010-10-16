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
package com.my.container.context;

/**
 * The Context Interface
 *
 * @author kevinpollet
 */
public interface Context {

    /**
     * Get a bean in the container
     * Context.
     *
     * @param clazz the bean class
     * @return the new bean instance
     */
    public <T> T getBean(final Class<T> clazz);

    /**
     * This method is used to inject dependencies in an
     * existing bean instance (Method and fields).
     *
     * @param bean the instance to be injected
     */
    public void resolveBeanDependencies(final Object bean);

    /**
     * Register a JVM ShutDown Hook.
     * All beans with PreDestroy methods
     * are called when the JVM shutdowns.
     */
    public void registerShutdownHook();

}

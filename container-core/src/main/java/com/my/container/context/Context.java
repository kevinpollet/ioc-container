package com.my.container.context;

import javax.inject.Qualifier;

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
     * Register a JVM ShutDown Hook.
     * All beans with PreDestroy methods
     * are called when the JVM shutdown.
     */
    public void registerShutdownHook();

}

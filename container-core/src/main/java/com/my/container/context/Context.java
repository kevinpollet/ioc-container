package com.my.container.context;

import javax.inject.Qualifier;

/**
 * The Context Interface
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
     * Permits to register a shutdown hook.
     * All bean PreDestroy methods are called
     * when the JVM shutdown.
     */
    public void registerShutdownHook();

}

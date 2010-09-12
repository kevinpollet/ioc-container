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

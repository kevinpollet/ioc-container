package com.my.container.spi;

/**
 * The bean processor contract. This
 * contract must be implemented by
 * extensions.
 *
 * @author kevinpollet
 */
public abstract class BeanProcessor {

    /**
     * Check if the bean is processable.
     *
     * @param bean the bean to process
     * @return true if the bean is processable or false otherwise
     */
    public abstract boolean isProcessable(final Object bean);

    /**
     * Process the bean in parameter. <b>This method
     * must never return null.</b>
     *
     * @param bean the bean to process
     * @param <T> the type of the bean
     * @return the bean processed
     * @throws Exception if an exception occurs during the bean processing
     */
    public abstract <T> T processBean(final T bean) throws Exception;

}

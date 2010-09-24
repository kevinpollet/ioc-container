package com.my.container.context.beanfactory;

import com.my.container.context.beanfactory.exceptions.BeanProcessorException;

/**
 * The bean invocation processor
 * contract.
 *
 * @author kevinpollet
 */
public abstract class InvocationProcessor {

    /**
     * Check if the bean is processable.
     *
     * @param bean the bean to process
     * @return true if the bean is processable or false otherwise
     */
    public abstract boolean isProcessable(final Object bean);

    /**
     * Process the bean in parameter.
     *
     * @param bean the bean to process
     * @param <T> the type of the bean
     * @return the bean processed
     * @throws BeanProcessorException if an exception occurs during the bean processing
     */
    public abstract <T> T processBean(final T bean) throws BeanProcessorException;

}

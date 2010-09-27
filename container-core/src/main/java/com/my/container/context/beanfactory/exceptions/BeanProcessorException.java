package com.my.container.context.beanfactory.exceptions;

/**
 * The bean processor exception. This exception
 * can be thrown by bean processor extensions.
 *
 * @author kevinpollet
 */
public class BeanProcessorException extends BeanException {

    /**
     * {@inheritDoc}
     */
    public BeanProcessorException(final String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public BeanProcessorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public BeanProcessorException(final Throwable cause) {
        super(cause);
    }
}

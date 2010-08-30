package com.my.container.context.beanfactory.exceptions;


/**
 * This exception is thrown when the
 * bean factory cannot inject a bean
 * dependency.
 *
 * @author kevinpollet
 */
public class BeanDependencyInjectionException extends BeanInstantiationException {

    public BeanDependencyInjectionException(String message) {
        super(message);
    }

    public BeanDependencyInjectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BeanDependencyInjectionException(final Throwable cause) {
        super(cause);
    }
}

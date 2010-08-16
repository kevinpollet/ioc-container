package com.my.container.exceptions.beanfactory;


/**
 * The exception throws when the bean definition
 * is not found in the context.
 *
 * @author kevinpollet
 */
public class BeanClassNotFoundException extends BeanFactoryException {

    public BeanClassNotFoundException(final String message) {
        super(message);
    }

    public BeanClassNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BeanClassNotFoundException(final Throwable cause) {
        super(cause);
    }
}

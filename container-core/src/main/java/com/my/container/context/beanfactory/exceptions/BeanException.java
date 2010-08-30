package com.my.container.context.beanfactory.exceptions;

/**
 * The abstract bean exception class. This class is the upper class
 * of all the exception relative to bean. 
 *
 * @author kevinpollet
 */
public abstract class BeanException extends RuntimeException {

    protected BeanException(final String message) {
        super(message);
    }

    protected BeanException(final String message, final Throwable cause) {
        super(message, cause);
    }

    protected BeanException(final Throwable cause) {
        super(cause);
    }
}

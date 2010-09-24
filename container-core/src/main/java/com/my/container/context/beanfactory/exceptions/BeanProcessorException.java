package com.my.container.context.beanfactory.exceptions;

/**
 * @author kevinpollet
 */
public class BeanProcessorException extends BeanException {

    public BeanProcessorException(String message) {
        super(message);
    }

    public BeanProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanProcessorException(Throwable cause) {
        super(cause);
    }
}

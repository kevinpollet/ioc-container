package com.my.container.context.beanfactory.exceptions;

/**
 * @author kevinpollet
 */
public class CallbackInvocationException extends BeanInstantiationException {

    public CallbackInvocationException(final String message) {
        super(message);
    }

    public CallbackInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallbackInvocationException(Throwable cause) {
        super(cause);
    }
}

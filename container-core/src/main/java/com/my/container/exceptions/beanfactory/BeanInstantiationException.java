package com.my.container.exceptions.beanfactory;

public class BeanInstantiationException extends BeanFactoryException {

    public BeanInstantiationException(String message) {
        super(message);
    }

    public BeanInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BeanInstantiationException(final Throwable cause) {
        super(cause);
    }
}

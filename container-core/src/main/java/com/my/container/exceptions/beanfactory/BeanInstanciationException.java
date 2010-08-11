package com.my.container.exceptions.beanfactory;

public class BeanInstanciationException extends BeanFactoryException {

    public BeanInstanciationException(String message) {
        super(message);
    }

    public BeanInstanciationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BeanInstanciationException(final Throwable cause) {
        super(cause);
    }
}

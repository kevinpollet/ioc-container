package com.my.container.context.beanfactory.exceptions;


/**
 * The exception throws when the bean definition
 * is not found in the context.
 *
 * @author kevinpollet
 */
public class NoSuchBeanDefinitionException extends BeanInstantiationException {

    public NoSuchBeanDefinitionException(final String message) {
        super(message);
    }

    public NoSuchBeanDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoSuchBeanDefinitionException(final Throwable cause) {
        super(cause);
    }
}

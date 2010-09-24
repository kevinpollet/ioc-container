package com.my.container.context.beanfactory;

/**
 * @author kevinpollet
 */
public abstract class BeanInstanceWeaver {

    public abstract boolean isValidBean(final Object bean);

    public abstract <T> T weaveBean(final T bean);

}

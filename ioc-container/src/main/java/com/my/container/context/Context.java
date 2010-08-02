package com.my.container.context;



//TODO add Qualifier
public interface Context {

    public <T> T getBean(final Class<T> clazz);
}

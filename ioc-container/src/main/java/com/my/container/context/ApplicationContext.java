package com.my.container.context;

import com.my.container.binding.Binding;
import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.beanfactory.BeanFactory;
import com.my.container.exceptions.beanfactory.BeanClassNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;


//TODO check callback  and interface
//TODO class sans interface ??
//TODO class interface abstract ??
public class ApplicationContext implements Context {

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    /**
     * The bean factory
     */
    private BeanFactory factory;

    /**
     * Construct the context.
     *
     * @param provider the provider of bindings
     */
    public ApplicationContext(final BindingProvider provider) {
        //Construct provider binding list
        provider.configureBindings();

        this.factory = new BeanFactory(provider.getBindings());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBean(final Class<T> clazz) {
        return this.factory.getBean(clazz);
    }

    //TODO PreDestroy and Shutdown Hook
    @Override
    public void registerShutdownHook() {

    }
}

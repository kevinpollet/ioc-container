package com.my.container.context;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.beanfactory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO class interface abstract ??

/**
 * The basic implementation of the
 * context interface.

 * @author kevinpollet
 */
public class ApplicationContext implements Context {

    private final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private BeanFactory factory;

    /**
     * Construct the context.
     *
     * @param provider the provider of bindings
     */
    public ApplicationContext(final BindingProvider provider) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolveBeanDependencies(final Object bean) {
        this.factory.resolveDependencies(bean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerShutdownHook() {
        Thread thread = new Thread(new CallbackShutdownHook(this.factory));
        Runtime.getRuntime().addShutdownHook(thread);
    }
}

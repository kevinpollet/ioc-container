package com.my.container.context.beanfactory;

import com.my.container.context.beanfactory.BeanFactory;


/**
 * This thread shutdown hook permits to
 * call the PreDestroy method on singleton
 * and prototype bean.
 *
 * {@inheritDoc}
 */
public class CallbackShutdownHook implements Runnable {

    private BeanFactory factory;


    public CallbackShutdownHook(final BeanFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        this.factory.removeAllBeansReferences();                                
    }
}

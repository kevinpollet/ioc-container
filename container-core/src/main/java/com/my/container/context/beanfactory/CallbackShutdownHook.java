package com.my.container.context.beanfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This thread shutdown hook permits to
 * call the PreDestroy method on singleton
 * and prototype bean.
 */
public class CallbackShutdownHook implements Runnable {

    private Logger logger = LoggerFactory.getLogger(CallbackShutdownHook.class);
    private BeanFactory factory;

    public CallbackShutdownHook(final BeanFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        logger.info("Shutdown hook called : Call all created bean PreDestroy methods");
        
        this.factory.removeAllBeansReferences();
    }
}

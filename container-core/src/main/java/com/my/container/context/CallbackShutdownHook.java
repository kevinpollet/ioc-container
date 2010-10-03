package com.my.container.context;

import com.my.container.context.beanfactory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This thread shutdown hook permits to
 * call the PreDestroy method on singleton
 * and prototype bean.
 *
 * @author kevinpollet
 */
public class CallbackShutdownHook implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(CallbackShutdownHook.class);
    
    private final BeanFactory factory;

    /**
     * The CallbackShutdown hook factory.
     *
     * @param factory the bean factory
     */
    public CallbackShutdownHook(final BeanFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        logger.info("Shutdown hook called : Call all created bean PreDestroy methods");
        this.factory.removeAllBeansReferences();
    }
}

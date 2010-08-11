package com.my.container.context;


/**
 * This thread shutdown hook permits to
 * call the PreDestroy method on singleton
 * and prototype bean.
 *
 * {@inheritDoc}
 */
public class CallbackShutdownHook implements Runnable {

    public CallbackShutdownHook() {
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        
    }
}

/*
 * Copyright 2010 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

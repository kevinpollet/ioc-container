/*
 * Copyright 2011 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.aop;

import com.my.container.aop.services.HelloService;
import com.my.container.aop.services.impl.HelloServiceWithAroundInvoke;
import com.my.container.binding.provider.FluentBindingProvider;
import com.my.container.core.Injector;
import com.my.container.util.ProxyHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevinpollet
 */
public class AroundInvokeTest {

    private Injector context;

    @Before
    public void setUp() {
        this.context = Injector.configure().addBindingProvider(new FluentBindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(HelloServiceWithAroundInvoke.class);
            }
        }).buildInjector();
    }

    @Test
    public void testAroundInvokeInterceptor() {
        HelloService service = this.context.getBean(HelloService.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("Hello", service.sayHello());
        Assert.assertEquals("Hello AroundInvoke", service.sayHello("AroundInvoke"));
        Assert.assertEquals(2, ((HelloServiceWithAroundInvoke) ProxyHelper.getTargetObject(service)).getBeforeCall());
        Assert.assertEquals(2, ((HelloServiceWithAroundInvoke) ProxyHelper.getTargetObject(service)).getAfterCall());
    }
    
}

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
package com.my.container.aop;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.core.Injector;
import com.my.container.aop.services.impl.HelloServiceWithInterceptor;
import com.my.container.aop.services.impl.MockInterceptor;
import com.my.container.aop.services.HelloService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * The injectservice before test.
 *
 * @author kevinpollet
 */
public class BeforeTest {

    private Injector context;

    @Before
    public void setUp() {
        this.context = Injector.configure().addBindingProvider( new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(HelloServiceWithInterceptor.class);
            }
        }).buildInjector();
    }

    @Test
    public void testBeforeInterceptor() throws NoSuchFieldException, IllegalAccessException {
        HelloService service = this.context.getBean(HelloService.class);

        Assert.assertNotNull(service);
        Assert.assertTrue(Proxy.isProxyClass(service.getClass()));

        //GetMock
        InterceptorInvocationHandler handler = (InterceptorInvocationHandler) Proxy.getInvocationHandler(service);
        Field field = handler.getClass().getDeclaredField("interceptors");
        field.setAccessible(true);

        Object[] interceptors = (Object[]) field.get(handler);
        MockInterceptor mock = (MockInterceptor)interceptors[0];

        //Call service
        service.sayHello();

        Assert.assertTrue(interceptors.length > 0);
        Assert.assertEquals(1, mock.getBeforeNbCall());
    }

}

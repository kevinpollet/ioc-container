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
package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.test.injection.services.*;
import com.my.container.test.injection.services.impl.*;
import com.my.container.test.injection.services.impl.constructors.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Injection in constructor.
 *
 * @author Kevin Pollet
 */
public class ConstructorInjectionTest {

    @Test
    public void testEmptyConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceC.class).to(ConstructorServiceCImpl.class);
            }
        });

        ServiceC serviceC = context.getBean(ServiceC.class);

        Assert.assertNotNull(serviceC);
        Assert.assertEquals("Echo", serviceC.echo("Echo"));
    }

    @Test
    public void testConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(ConstructorServiceDImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
            }
        });

        ServiceD serviceD = context.getBean(ServiceD.class);

        Assert.assertNotNull(serviceD);
        Assert.assertEquals("Hello", serviceD.echo("Hello"));
    }

    @Test
    public void testNamedConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceE.class).to(ConstructorQualifierServiceE.class);
                bind(ServiceC.class).to(ConstructorServiceCImpl.class);
                bind(ServiceC.class).to(UpperEchoServiceC.class).named("upperEchoService");
            }
        });

        ServiceE serviceE = context.getBean(ServiceE.class);

        Assert.assertNotNull(serviceE);
        Assert.assertEquals("ECHO", serviceE.echo("echo"));
    }

    @Test
    public void testQualifierConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceE.class).to(ConstructorNamedServiceE.class);
                bind(ServiceC.class).to(UpperEchoServiceC.class).named("upperEchoService");
                bind(ServiceC.class).to(UpperEchoServiceC.class).qualifiedBy(LowerEcho.class);
            }
        });

        ServiceE serviceE = context.getBean(ServiceE.class);

        Assert.assertNotNull(serviceE);
        Assert.assertEquals("ECHO", serviceE.echo("echo"));
    }

    @Test
    public void testUserProviderConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceE.class).to(ConstructorProviderServiceE.class);
                bind(ServiceC.class).toProvider(LowerEchoProvider.class);
            }
        });

        ServiceE serviceE = context.getBean(ServiceE.class);

        Assert.assertNotNull(serviceE);
        Assert.assertEquals("echo", serviceE.echo("ECHO"));
    }

    @Test
    public void testDefaultProviderConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceE.class).to(ConstructorProviderServiceE.class);
                bind(ServiceC.class).to(LowerEchoServiceC.class);
            }
        });

        ServiceE serviceE = context.getBean(ServiceE.class);

        Assert.assertNotNull(serviceE);
        Assert.assertEquals("echo", serviceE.echo("ECHO"));
    }

    @Test(expected = BeanInstantiationException.class)
    public void testCyclicConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(ConstructorServiceAImpl.class);
                bind(ServiceB.class).to(ConstructorServiceBImpl.class);
            }
        });

        context.getBean(ServiceA.class);
    }

}

package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceAImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceBImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceCImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceDImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceEImpl;
import com.my.container.test.injection.services.impl.defaults.LowerServiceC;
import com.my.container.test.injection.services.impl.defaults.UpperEchoServiceC;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the constructor injection.
 * 
 * @author kevinpollet
 */
public class ConstructorInjectionTest {

    @Test
    public void testConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(ConstructorServiceDImpl.class);
                bind(ServiceC.class).to(LowerServiceC.class);
            }
        });

        ServiceD serviceD = context.getBean(ServiceD.class);

        Assert.assertNotNull(serviceD);
        Assert.assertEquals("Hello", serviceD.echo("Hello"));
    }

    @Test(expected = BeanInstantiationException.class)
    public void testCyclicConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(ConstructorServiceAImpl.class);
                bind(ServiceB.class).to(ConstructorServiceBImpl.class);
            }
        });

        context.getBean(ServiceA.class);
    }

    @Test
    public void testDefaultConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider(){
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
    public void testNamedConstructorInjection() {
        Context context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(ServiceE.class).to(ConstructorServiceEImpl.class);
                bind(ServiceC.class).to(ConstructorServiceCImpl.class);
                bind(ServiceC.class).to(UpperEchoServiceC.class).named("upperEchoService");
            }
        });

        ServiceE serviceE = context.getBean(ServiceE.class);

        Assert.assertNotNull(serviceE);
        Assert.assertEquals("ECHO", serviceE.echo("echo"));
    }

}

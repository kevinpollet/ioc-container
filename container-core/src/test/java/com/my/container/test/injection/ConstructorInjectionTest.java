package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceAImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceBImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceCImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceDImpl;
import com.my.container.test.injection.services.impl.defaults.ServiceCImpl;
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
                bind(ServiceC.class).to(ServiceCImpl.class);
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

}

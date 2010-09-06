package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.injection.services.AbstractService;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceAImpl;
import com.my.container.test.injection.services.ServiceB;
import com.my.container.test.injection.services.ServiceBImpl;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceCImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;


/**
 * @author kevinpollet
 */
public class FieldInjectionTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(ServiceAImpl.class);
                bind(ServiceB.class).to(ServiceBImpl.class);
                bind(ServiceC.class).to(ServiceCImpl.class);
            }
        });
    }

    @Test
    public void testFieldDependencyInjection() throws NoSuchFieldException, IllegalAccessException {
        ServiceA service = this.context.getBean(ServiceA.class);

        //Get dependency
        Field depB = service.getClass().getDeclaredField("serviceB");
        depB.setAccessible(true);

        Field depC = service.getClass().getDeclaredField("serviceC");
        depC.setAccessible(true);

        Assert.assertNotNull(service);
        Assert.assertNotNull("Dependency is null", depB.get(service));
        Assert.assertNotNull("Dependency is null", depC.get(service));
        Assert.assertEquals("Hello Injection", service.sayHelloTo("Injection"));
        Assert.assertEquals("Great injection", service.echo("Great injection"));
    }

    @Test
    public void testSuperFieldDependencyInjection() throws NoSuchFieldException, IllegalAccessException {
        ServiceA service = this.context.getBean(ServiceA.class);

        Assert.assertNotNull(service);
        Assert.assertNotNull("SuperClass dependency is null", ((AbstractService)service).getServiceC());
        Assert.assertEquals("Hello Injection", service.sayHelloTo("Injection"));
    }

    @Test
    public void testCyclicDependencies() throws NoSuchFieldException, IllegalAccessException {
        ServiceA serviceA = this.context.getBean(ServiceA.class);

        //ServiceC dependency
        Field depB = ServiceAImpl.class.getDeclaredField("serviceB");
        depB.setAccessible(true);

        ServiceB serviceB = (ServiceB) depB.get(serviceA);
        Field depA = ServiceBImpl.class.getDeclaredField("serviceA");
        depA.setAccessible(true);

        Assert.assertNotNull(serviceA);
        Assert.assertNotNull(serviceB);
        Assert.assertSame("The objects have not the same reference in the cycle", serviceA, depA.get(serviceB));
    }

}

package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.impl.EchoServiceC;
import com.my.container.test.injection.services.impl.LowerEcho;
import com.my.container.test.injection.services.impl.LowerEchoServiceC;
import com.my.container.test.injection.services.impl.UpperEchoServiceC;
import com.my.container.test.injection.services.impl.fields.*;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Field;


/**
 * @author kevinpollet
 */
public class FieldInjectionTest {

    @Test
    public void testFieldDependencyInjection() throws NoSuchFieldException, IllegalAccessException {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(FieldServiceAImpl.class);
                bind(ServiceB.class).to(FieldServiceBImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceD.class).to(FieldNamedServiceD.class);
            }
        });

        ServiceA service = context.getBean(ServiceA.class);

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
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(FieldServiceAImpl.class);
                bind(ServiceB.class).to(FieldServiceBImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceD.class).to(FieldNamedServiceD.class);
            }
        });

        ServiceA service = context.getBean(ServiceA.class);

        Assert.assertNotNull(service);
        Assert.assertNotNull("SuperClass dependency is null", ((FieldAbstractService) service).getServiceC());
        Assert.assertEquals("Hello Injection", service.sayHelloTo("Injection"));
    }

    @Test
    public void testCyclicDependencies() throws NoSuchFieldException, IllegalAccessException {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(FieldServiceAImpl.class);
                bind(ServiceB.class).to(FieldServiceBImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceD.class).to(FieldNamedServiceD.class);
            }
        });

        ServiceA serviceA = context.getBean(ServiceA.class);

        //ServiceC dependency
        Field depB = FieldServiceAImpl.class.getDeclaredField("serviceB");
        depB.setAccessible(true);

        ServiceB serviceB = (ServiceB) depB.get(serviceA);
        Field depA = FieldServiceBImpl.class.getDeclaredField("serviceA");
        depA.setAccessible(true);

        Assert.assertNotNull(serviceA);
        Assert.assertNotNull(serviceB);
        Assert.assertSame("The objects have not the same reference in the cycle", serviceA, depA.get(serviceB));
    }

    @Test
    public void testExistingBeanFieldInjection() throws NoSuchFieldException, IllegalAccessException {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(FieldServiceAImpl.class);
                bind(ServiceB.class).to(FieldServiceBImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceD.class).to(FieldNamedServiceD.class);
            }
        });

        ServiceA service = new FieldServiceAImpl();
        context.resolveBeanDependencies(service);

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
    public void testNamedBindingBeanFieldInjection() throws NoSuchFieldException, IllegalAccessException {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceC.class).to(UpperEchoServiceC.class).named("upperEchoService");
                bind(ServiceD.class).to(FieldNamedServiceD.class);
            }
        });

        ServiceD serviceD = context.getBean(ServiceD.class);

        Assert.assertNotNull(serviceD);
        Assert.assertEquals("ECHO", serviceD.echo("echo"));
    }

    @Test
    public void testQualifiedBindingBeanFieldInjection() throws NoSuchFieldException, IllegalAccessException {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceC.class).to(LowerEchoServiceC.class).qualifiedBy(LowerEcho.class);
                bind(ServiceD.class).to(FieldQualifierServiceD.class);
            }
        });

        ServiceD serviceD = context.getBean(ServiceD.class);

        Assert.assertNotNull(serviceD);
        Assert.assertEquals("echo", serviceD.echo("ECHO"));
    }

}

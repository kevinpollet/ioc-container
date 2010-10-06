package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;
import com.my.container.test.injection.services.impl.EchoServiceC;
import com.my.container.test.injection.services.impl.LowerEcho;
import com.my.container.test.injection.services.impl.LowerEchoServiceC;
import com.my.container.test.injection.services.impl.UpperEchoServiceC;
import com.my.container.test.injection.services.impl.methods.MethodNamedServiceA;
import com.my.container.test.injection.services.impl.methods.MethodQualifierServiceA;
import com.my.container.test.injection.services.impl.methods.MethodServiceDImpl;
import com.my.container.test.injection.services.impl.methods.MethodServiceEImpl;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author kevinpollet
 */
public class MethodInjectionTest {

    @Test
    public void testMethodInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(MethodServiceDImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceD service = context.getBean(ServiceD.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("Great Method Injection", service.echo("Great Method Injection"));
    }

    @Test
    public void testCyclicMethodInjection() throws NoSuchFieldException, IllegalAccessException {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(MethodServiceDImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceD serviceD = context.getBean(ServiceD.class);

        //Get depE for serviceD
        Field depE = MethodServiceDImpl.class.getDeclaredField("serviceE");
        depE.setAccessible(true);

        //Get depD for serviceE
        Field depD = MethodServiceEImpl.class.getDeclaredField("serviceD");
        depD.setAccessible(true);


        Assert.assertNotNull(serviceD);
        Assert.assertEquals(2, serviceD.add(1, 1));
        Assert.assertEquals("Hello", ((ServiceE) depE.get(serviceD)).echo("Hello"));
        Assert.assertSame(serviceD, depD.get(depE.get(serviceD)));

    }

    @Test
    public void testInjectOverrideInject() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(MethodServiceDImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceD service = context.getBean(ServiceD.class);

        Assert.assertNotNull(service);
        Assert.assertEquals(1, ((MethodServiceDImpl) service).getNbCallSetServiceC());
    }

    @Test
    public void testNoInjectOverrideInject() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(MethodServiceDImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceE service = context.getBean(ServiceE.class);

        Assert.assertNotNull(service);
        Assert.assertEquals(0, ((MethodServiceEImpl) service).getNbCallSetServiceC());
    }

    @Test
    public void testExistingMethodInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(MethodServiceDImpl.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceD service = new MethodServiceDImpl();
        context.resolveBeanDependencies(service);

        Assert.assertNotNull(service);
        Assert.assertEquals("Great Method Injection", service.echo("Great Method Injection"));
    }

    @Test
    public void testNamedMethodInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(MethodNamedServiceA.class);
                bind(ServiceC.class).to(EchoServiceC.class);
                bind(ServiceC.class).to(UpperEchoServiceC.class).named("upperEchoService");
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceA service = context.getBean(ServiceA.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("ECHO", service.echo("echo"));
    }

    @Test
    public void testQualifiedMethodInjection() {
        Context context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(ServiceA.class).to(MethodQualifierServiceA.class);
                bind(ServiceC.class).to(UpperEchoServiceC.class).named("upperEchoService");
                bind(ServiceC.class).to(LowerEchoServiceC.class).qualifiedBy(LowerEcho.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });

        ServiceA service = context.getBean(ServiceA.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("echo", service.echo("ECHO"));
    }

}

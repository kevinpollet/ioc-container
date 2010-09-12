package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;
import com.my.container.test.injection.services.impl.defaults.ServiceCImpl;
import com.my.container.test.injection.services.impl.methods.MethodServiceDImpl;
import com.my.container.test.injection.services.impl.methods.MethodServiceEImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author kevinpollet
 */
public class MethodInjectionTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(ServiceD.class).to(MethodServiceDImpl.class);
                bind(ServiceC.class).to(ServiceCImpl.class);
                bind(ServiceE.class).to(MethodServiceEImpl.class);
            }
        });
    }

    @Test
    public void testMethodInjection() {
        ServiceD service = this.context.getBean(ServiceD.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("Great Method Injection", service.echo("Great Method Injection"));        
    }

    @Test
    public void testCyclicMethodInjection() throws NoSuchFieldException, IllegalAccessException {
        ServiceD serviceD = this.context.getBean(ServiceD.class);

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
        ServiceD service = this.context.getBean(ServiceD.class);

        Assert.assertNotNull(service);
        Assert.assertEquals(1, ((MethodServiceDImpl) service).getNbCallSetServiceC());
    }

    @Test
    public void testNoInjectOverrideInject() {
        ServiceE service = this.context.getBean(ServiceE.class);

        Assert.assertNotNull(service);
        Assert.assertEquals(0, ((MethodServiceEImpl) service).getNbCallSetServiceC());
    }

    @Test
    public void testExistingMethodInjection() {
        ServiceD service = new MethodServiceDImpl();
        this.context.resolveBeanDependencies(service);

        Assert.assertNotNull(service);
        Assert.assertEquals("Great Method Injection", service.echo("Great Method Injection"));
    }

}

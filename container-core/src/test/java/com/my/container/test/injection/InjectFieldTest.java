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
 *         Date: 17 août 2010
 */
public class InjectFieldTest {

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
        Field dependency = service.getClass().getDeclaredField("serviceB");
        dependency.setAccessible(true);

        Assert.assertNotNull(service);
        Assert.assertNotNull("Dependency is null", dependency.get(service));
        Assert.assertEquals("Hello Injection", service.sayHelloTo("Injection"));
    }

    @Test
    public void testSuperFieldDependencyInjection() throws NoSuchFieldException, IllegalAccessException {
        ServiceA service = this.context.getBean(ServiceA.class);

        Assert.assertNotNull(service);
        Assert.assertNotNull("SuperClass dependency is null", ((AbstractService)service).getServiceC());
        Assert.assertEquals("Hello Injection", service.sayHelloTo("Injection"));
    }

    @Test
    public void testCyclicDepedencies() throws NoSuchFieldException, IllegalAccessException {
        ServiceA serviceA = this.context.getBean(ServiceA.class);

        //ServiceC dependency
        Field dependencyC = AbstractService.class.getDeclaredField("serviceC");
        dependencyC.setAccessible(true);

        ServiceC serviceC = (ServiceC) dependencyC.get(serviceA);
        Field dependencyA = serviceC.getClass().getDeclaredField("serviceA");
        dependencyA.setAccessible(true);

        Assert.assertNotNull(serviceA);
        Assert.assertNotNull(serviceC);
        Assert.assertSame("The objects have not the same reference in the cycle", serviceA, dependencyA.get(serviceC));
    }

}

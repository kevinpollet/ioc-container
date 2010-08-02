package com.my.container.test;

import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.services.Service;
import com.my.container.services.ServiceBinder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContainerTest {

    private Context ctxt;

    @Before
    public void setUp() {
        this.ctxt = new ApplicationContext(new ServiceBinder());
    }

    @Test
    public void testBeanCreation() {
        Service service = this.ctxt.getBean(Service.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("Hello", service.sayHello());
    }

    @Test
    public void testSingleton() {
        Service service = this.ctxt.getBean(Service.class);
        Service service2 = this.ctxt.getBean(Service.class);

        Assert.assertNotNull(service);
        Assert.assertNotNull(service2);
        Assert.assertTrue(service == service2);
    }


}

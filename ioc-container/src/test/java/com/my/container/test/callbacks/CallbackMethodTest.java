package com.my.container.test.callbacks;

import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.callbacks.binder.BindingProviderTest;
import com.my.container.test.callbacks.services.Service;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class CallbackMethodTest {

    private Context ctxt;

    @Before
    public void setUp() {
        this.ctxt = new ApplicationContext(new BindingProviderTest());
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

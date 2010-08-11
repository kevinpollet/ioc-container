package com.my.container.test.callbacks;

import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.env.binder.ServiceBindingProvider;
import com.my.container.env.services.HelloService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class CallbackMethodTest {

    private Context ctxt;

    @Before
    public void setUp() {
        this.ctxt = new ApplicationContext(new ServiceBindingProvider());
    }

    @Test
    public void testPostConstruct() {
        HelloService helloService = this.ctxt.getBean(HelloService.class);

        Assert.assertNotNull(helloService);
        Assert.assertEquals("HelloConstruct", helloService.sayHello());
    }

}

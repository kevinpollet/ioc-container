package com.my.container.test.scope;

import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.env.binder.ServiceBindingProvider;
import com.my.container.env.services.HelloService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the bean scope singleton.
 */
public class SingletonScopeTest {

    private Context context;

    @Before
    public void setUp() {
       this.context = new ApplicationContext(new ServiceBindingProvider());
    }

    @Test
    public void testSingleton() {
        HelloService helloService = this.context.getBean(HelloService.class);
        HelloService service2 = this.context.getBean(HelloService.class);

        Assert.assertNotNull(helloService);
        Assert.assertNotNull(service2);
        Assert.assertTrue(helloService == service2);
    }
}

package com.my.container.test.scope;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.services.basic.Service;
import com.my.container.services.basic.ServiceImpl;
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
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(Service.class).to(ServiceImpl.class);
            }
        });

    }

    @Test
    public void testSingletonScope() {
        Service service = this.context.getBean(Service.class);
        Service service2 = this.context.getBean(Service.class);

        Assert.assertNotNull(service);
        Assert.assertNotNull(service2);
        Assert.assertTrue(service == service2);
    }
}

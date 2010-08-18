package com.my.container.test.callbacks;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.services.basic.Service;
import com.my.container.services.basic.ServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the PostConstruct callback.
 *
 * @author kevinpollet
 */
public class PostConstructTest {

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
    public void testPostConstruct() {
        Service service = this.context.getBean(Service.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("HelloConstruct", service.sayHello());
    }

}

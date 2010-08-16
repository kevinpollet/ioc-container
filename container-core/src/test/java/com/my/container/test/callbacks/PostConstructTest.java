package com.my.container.test.callbacks;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.env.services.HelloService;
import com.my.container.env.services.HelloServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class PostConstructTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(HelloServiceImpl.class);
            }
        });
    }

    @Test
    public void testPostConstruct() {

        HelloService helloService = this.context.getBean(HelloService.class);

        Assert.assertNotNull(helloService);
        Assert.assertEquals("HelloConstruct", helloService.sayHello());
    }

}

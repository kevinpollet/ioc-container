package com.my.container.test.interceptors;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.proxy.ProxyHelper;
import com.my.container.test.interceptors.services.HelloService;
import com.my.container.test.interceptors.services.impl.HelloServiceWithAroundInvoke;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevinpollet
 */
public class AroundInvokeTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(HelloServiceWithAroundInvoke.class);
            }
        });
    }

    @Test
    public void testAroundInvokeInterceptor() {
        HelloService service = this.context.getBean(HelloService.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("Hello", service.sayHello());
        Assert.assertEquals("Hello AroundInvoke", service.sayHello("AroundInvoke"));
        Assert.assertEquals(2, ((HelloServiceWithAroundInvoke) ProxyHelper.getTargetObject(service)).getBeforeCall());
        Assert.assertEquals(2, ((HelloServiceWithAroundInvoke) ProxyHelper.getTargetObject(service)).getAfterCall());
    }
    
}

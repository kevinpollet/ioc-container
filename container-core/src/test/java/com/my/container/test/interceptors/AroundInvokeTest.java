package com.my.container.test.interceptors;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.proxy.ProxyHelper;
import com.my.container.test.interceptors.services.AroundInvokeServiceImpl;
import com.my.container.test.interceptors.services.Service;
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
                bind(Service.class).to(AroundInvokeServiceImpl.class);
            }
        });
    }

    @Test
    public void testAroundInvoke() {
        Service service = this.context.getBean(Service.class);

        Assert.assertNotNull(service);
        Assert.assertEquals("Hello", service.sayHello());
        Assert.assertEquals("Hello AroundInvoke", service.sayHello("AroundInvoke"));
        Assert.assertEquals(2, ((AroundInvokeServiceImpl) ProxyHelper.getTargetObject(service)).getBeforeCall());
        Assert.assertEquals(2, ((AroundInvokeServiceImpl) ProxyHelper.getTargetObject(service)).getAfterCall());
    }
    
}

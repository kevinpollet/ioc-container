package com.my.container.test.interceptors;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.proxy.InterceptorInvocationHandler;
import com.my.container.services.MockInterceptor;
import com.my.container.services.basic.Service;
import com.my.container.services.basic.ServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * The injectservice before test.
 *
 * @author kevinpollet
 */
public class BeforeTest {

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
    public void testBeforeInterceptor() throws NoSuchFieldException, IllegalAccessException {
        Service service = this.context.getBean(Service.class);

        Assert.assertNotNull(service);
        Assert.assertTrue(Proxy.isProxyClass(service.getClass()));

        //GetMock
        InterceptorInvocationHandler handler = (InterceptorInvocationHandler) Proxy.getInvocationHandler(service);
        Field field = handler.getClass().getDeclaredField("interceptors");
        field.setAccessible(true);

        Object[] interceptors = (Object[]) field.get(handler);
        MockInterceptor mock = (MockInterceptor)interceptors[0];

        //Call service
        service.sayHello();

        Assert.assertTrue(interceptors.length > 0);
        Assert.assertEquals(1, mock.getBeforeNbCall());
    }

}

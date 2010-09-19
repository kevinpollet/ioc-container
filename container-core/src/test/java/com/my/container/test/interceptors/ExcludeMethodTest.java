package com.my.container.test.interceptors;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.interceptors.BeanInterceptorInvocationHandler;
import com.my.container.test.interceptors.services.HelloService;
import com.my.container.test.interceptors.services.impl.HelloServiceWithInterceptor;
import com.my.container.test.interceptors.services.impl.MockInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author kevinpollet
 */
public class ExcludeMethodTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(HelloServiceWithInterceptor.class);
            }
        });
    }

    @Test
    public void testExcludeMethodInterceptor() throws NoSuchFieldException, IllegalAccessException {
        HelloService service = this.context.getBean(HelloService.class);

        Assert.assertNotNull(service);
        Assert.assertTrue(Proxy.isProxyClass(service.getClass()));

        //GetMock
        BeanInterceptorInvocationHandler handler = (BeanInterceptorInvocationHandler) Proxy.getInvocationHandler(service);
        Field field = handler.getClass().getDeclaredField("interceptors");
        field.setAccessible(true);

        Object[] interceptors = (Object[]) field.get(handler);
        MockInterceptor mock = (MockInterceptor)interceptors[0];

        //Call service
        service.sayHello("Test");

        Assert.assertTrue(interceptors.length > 0);
        Assert.assertEquals(0, mock.getBeforeNbCall());
        Assert.assertEquals(0, mock.getAfterNbCall());
    }
    
}

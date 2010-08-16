package com.my.container.test.callbacks;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.BeanFactory;
import com.my.container.env.services.HelloService;
import com.my.container.env.services.HelloServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * The PreDestroy annotation test.
 *
 * @author kevinpollet
 *         Date: 16 août 2010
 */
public class PreDestroyTest {

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
    public void testPreDestroy() throws NoSuchFieldException, IllegalAccessException {
        HelloService helloService = this.context.getBean(HelloService.class);

        //Get the private bean factory
        Field factoryField = this.context.getClass().getDeclaredField("factory");
        factoryField.setAccessible(true);
        BeanFactory factory = (BeanFactory) factoryField.get(this.context);
        factory.removeAllBeansReferences();

        Assert.assertNotNull(helloService);
        Assert.assertEquals("HelloDestroy", helloService.sayHello());
    }

}

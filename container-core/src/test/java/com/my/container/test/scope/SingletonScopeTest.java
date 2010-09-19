package com.my.container.test.scope;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.scope.services.HelloService;
import com.my.container.test.scope.services.impl.SingletonHelloService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the singleton scope.
 *
 * @author kevinpollet
 */
public class SingletonScopeTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(SingletonHelloService.class);
            }
        });

    }

    @Test
    public void testSingletonScope() {
        HelloService firstInstance = this.context.getBean(HelloService.class);
        HelloService secondInstance = this.context.getBean(HelloService.class);

        Assert.assertNotNull(firstInstance);
        Assert.assertNotNull(secondInstance);
        Assert.assertSame("Beans have not the same reference", firstInstance, secondInstance);
        Assert.assertEquals("Hello", firstInstance.sayHello());
        Assert.assertEquals("Hello", secondInstance.sayHello());
    }
}

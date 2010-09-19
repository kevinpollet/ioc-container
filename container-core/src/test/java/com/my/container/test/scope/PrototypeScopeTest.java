package com.my.container.test.scope;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.scope.services.HelloService;
import com.my.container.test.scope.services.impl.PrototypeHelloService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the prototype scope test.
 *
 * @author kevinpollet
 */
public class PrototypeScopeTest {

     private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(HelloService.class).to(PrototypeHelloService.class);
            }
        });

    }

    @Test
    public void testSingletonScope() {
        HelloService firstInstance = this.context.getBean(HelloService.class);
        HelloService secondInstance = this.context.getBean(HelloService.class);

        Assert.assertNotNull(firstInstance);
        Assert.assertNotNull(secondInstance);
        Assert.assertNotSame("Beans have the same reference", firstInstance, secondInstance);
        Assert.assertEquals("Hello", firstInstance.sayHello());
        Assert.assertEquals("Hello", secondInstance.sayHello());
    }
    
}

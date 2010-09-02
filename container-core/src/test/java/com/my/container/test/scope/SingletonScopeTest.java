package com.my.container.test.scope;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.scope.services.Service;
import com.my.container.test.scope.services.SingletonServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the bean scope singleton.
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
                bind(Service.class).to(SingletonServiceImpl.class);
            }
        });

    }

    @Test
    public void testSingletonScope() {
        Service firstInstance = this.context.getBean(Service.class);
        Service secondInstance = this.context.getBean(Service.class);

        Assert.assertNotNull(firstInstance);
        Assert.assertNotNull(secondInstance);
        Assert.assertSame("Beans have not the same reference", firstInstance, secondInstance);
    }
}

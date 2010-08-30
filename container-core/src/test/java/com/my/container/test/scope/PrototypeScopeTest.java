package com.my.container.test.scope;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.scope.services.PrototypeServiceImpl;
import com.my.container.test.scope.services.Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The prototype scope test.
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
                bind(Service.class).to(PrototypeServiceImpl.class);
            }
        });

    }

    @Test
    public void testSingletonScope() {
        Service firstInstance = this.context.getBean(Service.class);
        Service secondInstance = this.context.getBean(Service.class);

        Assert.assertNotNull(firstInstance);
        Assert.assertNotNull(secondInstance);
        Assert.assertNotSame("Beans have the same reference", firstInstance, secondInstance);
    }
    
}

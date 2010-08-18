package com.my.container.test.injection;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.services.injectservice.InjectService;
import com.my.container.services.injectservice.InjectServiceImpl;
import com.my.container.services.basic.Service;
import com.my.container.services.basic.ServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevinpollet
 *         Date: 17 août 2010
 */
public class InjectFieldTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(Service.class).to(ServiceImpl.class);
                bind(InjectService.class).to(InjectServiceImpl.class);
            }
        });
    }

    @Test
    public void testFieldDependencyInjection() {
        InjectService injectService = this.context.getBean(InjectService.class);

        Assert.assertNotNull(injectService);
        Assert.assertEquals("HelloConstruct", injectService.sayHello());
    }

    @Test
    public void testFieldSuperclassDependencyInjection() {
        InjectService injectService = this.context.getBean(InjectService.class);

        Assert.assertNotNull(injectService);
        Assert.assertEquals("HelloConstructtoto", injectService.sayHello("toto"));
    }
}

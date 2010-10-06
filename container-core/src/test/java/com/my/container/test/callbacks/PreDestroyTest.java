package com.my.container.test.callbacks;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.context.beanfactory.BeanFactory;
import com.my.container.test.callbacks.services.Leaf;
import com.my.container.test.callbacks.services.Parent;
import com.my.container.test.callbacks.services.impl.LeafImpl;
import com.my.container.test.callbacks.services.impl.ParentImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * The PreDestroy callback test.
 *
 * @author kevinpollet
 */
public class PreDestroyTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider() {
            @Override
            public void configureBindings() {
                bind(Parent.class).to(ParentImpl.class);
                bind(Leaf.class).to(LeafImpl.class);
            }
        });
    }

    @Test
    public void testPreDestroy() throws NoSuchFieldException, IllegalAccessException {
        Parent parent = this.context.getBean(Parent.class);

        //Get the private bean factory
        Field factoryField = this.context.getClass().getDeclaredField("factory");
        factoryField.setAccessible(true);
        BeanFactory factory = (BeanFactory) factoryField.get(this.context);
        factory.removeAllBeansReferences();

        Assert.assertNotNull(parent);
        Assert.assertNotNull(parent);
        Assert.assertEquals("PreDestroy method not called or more than one times", 1, ((ParentImpl) parent).getNbCallPreDestroy());
    }

}

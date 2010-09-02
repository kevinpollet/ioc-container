package com.my.container.test.callbacks;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.context.ApplicationContext;
import com.my.container.context.Context;
import com.my.container.test.callbacks.services.Leaf;
import com.my.container.test.callbacks.services.LeafImpl;
import com.my.container.test.callbacks.services.Parent;
import com.my.container.test.callbacks.services.ParentImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the PostConstruct callback.
 *
 * @author kevinpollet
 */
public class PostConstructTest {

    private Context context;

    @Before
    public void setUp() {
        this.context = new ApplicationContext(new BindingProvider(){
            @Override
            public void configureBindings() {
                bind(Parent.class).to(ParentImpl.class);
                bind(Leaf.class).to(LeafImpl.class);
            }
        });
    }

    @Test
    public void testPostConstruct() {
        Parent parent = this.context.getBean(Parent.class);

        Assert.assertNotNull(parent);
        Assert.assertEquals("PostConstruct method not called or more than one times", 1, ((ParentImpl) parent).getNbCallPostConstruct());
    }

    @Test
    public void testPostConstructCalledAfterAllInjections() {
        Parent parent = this.context.getBean(Parent.class);

        Assert.assertNotNull(parent);
        Assert.assertEquals("Parent", parent.getReference());
        Assert.assertEquals("Leaf", parent.getLeafReference());
    }

}

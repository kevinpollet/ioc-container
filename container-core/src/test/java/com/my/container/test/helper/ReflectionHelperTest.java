package com.my.container.test.helper;

import com.my.container.test.helper.services.impl.AbstractBean;
import com.my.container.test.helper.services.impl.ConcreteBeanImpl;
import com.my.container.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the helper class behaviour.
 *
 * @author kevinpollet
 */
public class ReflectionHelperTest {


    @Test
    public void testMethodNotOverridedIsOverriden() throws NoSuchMethodException {
        Assert.assertFalse(ReflectionHelper.isOverridden(ConcreteBeanImpl.class, ConcreteBeanImpl.class.getDeclaredMethod("getNotOverriddenText")));
    }

    @Test
    public void testPublicMethodIsOverriden() throws NoSuchMethodException {
        Assert.assertTrue(ReflectionHelper.isOverridden(ConcreteBeanImpl.class, AbstractBean.class.getDeclaredMethod("getOverriddenPublicText")));
    }

    @Test
    public void testProtectedMethodIsOverriden() throws NoSuchMethodException {
        Assert.assertTrue(ReflectionHelper.isOverridden(ConcreteBeanImpl.class, AbstractBean.class.getDeclaredMethod("getOverriddenProtectedText")));
    }

}

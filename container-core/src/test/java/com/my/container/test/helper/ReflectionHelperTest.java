package com.my.container.test.helper;

import com.my.container.test.helper.services.AbstractBean;
import com.my.container.test.helper.services.ConcreteBeanImpl;
import com.my.container.util.ReflectionHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kevinpollet
 */
public class ReflectionHelperTest {


    @Test
    public void testMethodNotOverridedIsOverriden() throws NoSuchMethodException {
        Assert.assertFalse(ReflectionHelper.isOverriden(ConcreteBeanImpl.class, ConcreteBeanImpl.class.getDeclaredMethod("getSecondText")));
    }

    @Test
    public void testPublicMethodIsOverriden() throws NoSuchMethodException {
        Assert.assertTrue(ReflectionHelper.isOverriden(ConcreteBeanImpl.class, AbstractBean.class.getDeclaredMethod("getText")));
    }

    @Test
    public void testProtectedMethodIsOverriden() throws NoSuchMethodException {
        Assert.assertTrue(ReflectionHelper.isOverriden(ConcreteBeanImpl.class, AbstractBean.class.getDeclaredMethod("getProtectedText")));
    }

}

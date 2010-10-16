/*
 * Copyright 2010 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

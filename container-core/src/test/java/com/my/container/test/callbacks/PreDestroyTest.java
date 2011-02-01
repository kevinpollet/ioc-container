/*
 * Copyright 2011 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.my.container.test.callbacks;

import java.lang.reflect.Field;

import com.my.container.Configuration;
import com.my.container.Container;
import com.my.container.binding.FluentBindingProvider;
import com.my.container.engine.BeanStoreImpl;
import com.my.container.test.callbacks.services.Leaf;
import com.my.container.test.callbacks.services.Parent;
import com.my.container.test.callbacks.services.impl.LeafImpl;
import com.my.container.test.callbacks.services.impl.ParentImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The PreDestroy callback test.
 *
 * @author Kevin Pollet
 */
public class PreDestroyTest {

	private Container injector;

	@Before
	public void setUp() {
		Configuration config = Container.configure();
		config.addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( Parent.class ).to( ParentImpl.class );
						bind( Leaf.class ).to( LeafImpl.class );
					}
				}
		);

		injector = config.buildContainer();
	}

	@Test
	public void testPreDestroy() throws NoSuchFieldException, IllegalAccessException {
		Parent parent = injector.get( Parent.class );

		//Get the private bean factory
		Field factoryField = injector.getClass().getDeclaredField( "beanStore" );
		factoryField.setAccessible( true );
		BeanStoreImpl factory = (BeanStoreImpl) factoryField.get( injector );
		factory.destroy();

		Assert.assertNotNull( parent );
		Assert.assertNotNull( parent );
		Assert.assertEquals(
				"PreDestroy method not called or more than one times", 1, ( (ParentImpl) parent ).getNbCallPreDestroy()
		);
	}

}

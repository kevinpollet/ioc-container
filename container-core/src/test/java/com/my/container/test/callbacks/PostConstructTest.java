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

import com.my.container.Container;
import com.my.container.binding.FluentBindingProvider;
import com.my.container.Configuration;
import com.my.container.test.callbacks.services.Leaf;
import com.my.container.test.callbacks.services.Parent;
import com.my.container.test.callbacks.services.impl.LeafImpl;
import com.my.container.test.callbacks.services.impl.ParentImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the PostConstruct callback.
 *
 * @author Kevin Pollet
 */
public class PostConstructTest {

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
	public void testPostConstruct() {
		Parent parent = injector.get( Parent.class );

		Assert.assertNotNull( parent );
		Assert.assertEquals(
				"PostConstruct method not called or more than one times",
				1,
				( (ParentImpl) parent ).getNbCallPostConstruct()
		);
	}

	@Test
	public void testPostConstructCalledAfterAllInjections() {
		Parent parent = injector.get( Parent.class );

		Assert.assertNotNull( parent );
		Assert.assertEquals( "Parent", parent.getName() );
		Assert.assertEquals( "Leaf", parent.getLeafName() );
	}

}

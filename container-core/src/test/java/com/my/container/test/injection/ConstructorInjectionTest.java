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
package com.my.container.test.injection;

import com.my.container.exceptions.BeanInstantiationException;
import com.my.container.Injector;
import com.my.container.binding.provider.FluentBindingProvider;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceB;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;
import com.my.container.test.injection.services.impl.EchoServiceC;
import com.my.container.test.injection.services.impl.LowerEcho;
import com.my.container.test.injection.services.impl.LowerEchoProvider;
import com.my.container.test.injection.services.impl.LowerEchoServiceC;
import com.my.container.test.injection.services.impl.UpperEchoServiceC;
import com.my.container.test.injection.services.impl.constructors.ConstructorNamedServiceE;
import com.my.container.test.injection.services.impl.constructors.ConstructorProviderServiceE;
import com.my.container.test.injection.services.impl.constructors.ConstructorQualifierServiceE;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceAImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceBImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceCImpl;
import com.my.container.test.injection.services.impl.constructors.ConstructorServiceDImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Injection in constructor.
 *
 * @author Kevin Pollet
 */
public class ConstructorInjectionTest {

	@Test
	public void testEmptyConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceC.class ).to( ConstructorServiceCImpl.class );
					}
				}
		).buildInjector();

		ServiceC serviceC = injector.get( ServiceC.class );

		Assert.assertNotNull( serviceC );
		Assert.assertEquals( "Echo", serviceC.echo( "Echo" ) );
	}

	@Test
	public void testConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceD.class ).to( ConstructorServiceDImpl.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
					}
				}
		).buildInjector();

		ServiceD serviceD = injector.get( ServiceD.class );

		Assert.assertNotNull( serviceD );
		Assert.assertEquals( "Hello", serviceD.echo( "Hello" ) );
	}

	@Test
	public void testNamedConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceE.class ).to( ConstructorQualifierServiceE.class );
						bind( ServiceC.class ).to( ConstructorServiceCImpl.class );
						bind( ServiceC.class ).to( UpperEchoServiceC.class ).named( "upperEchoService" );
					}
				}
		).buildInjector();

		ServiceE serviceE = injector.get( ServiceE.class );

		Assert.assertNotNull( serviceE );
		Assert.assertEquals( "ECHO", serviceE.echo( "echo" ) );
	}

	@Test
	public void testQualifierConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceE.class ).to( ConstructorNamedServiceE.class );
						bind( ServiceC.class ).to( UpperEchoServiceC.class ).named( "upperEchoService" );
						bind( ServiceC.class ).to( UpperEchoServiceC.class ).qualifiedBy( LowerEcho.class );
					}
				}
		).buildInjector();

		ServiceE serviceE = injector.get( ServiceE.class );

		Assert.assertNotNull( serviceE );
		Assert.assertEquals( "ECHO", serviceE.echo( "echo" ) );
	}

	@Test
	public void testUserProviderConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceE.class ).to( ConstructorProviderServiceE.class );
						bind( ServiceC.class ).toProvider( LowerEchoProvider.class );
					}
				}
		).buildInjector();

		ServiceE serviceE = injector.get( ServiceE.class );

		Assert.assertNotNull( serviceE );
		Assert.assertEquals( "echo", serviceE.echo( "ECHO" ) );
	}

	@Test
	public void testDefaultProviderConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceE.class ).to( ConstructorProviderServiceE.class );
						bind( ServiceC.class ).to( LowerEchoServiceC.class );
					}
				}
		).buildInjector();

		ServiceE serviceE = injector.get( ServiceE.class );

		Assert.assertNotNull( serviceE );
		Assert.assertEquals( "echo", serviceE.echo( "ECHO" ) );
	}

	@Test(expected = BeanInstantiationException.class)
	public void testCyclicConstructorInjection() {
		Injector injector = Injector.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceA.class ).to( ConstructorServiceAImpl.class );
						bind( ServiceB.class ).to( ConstructorServiceBImpl.class );
					}
				}
		).buildInjector();

		injector.get( ServiceA.class );
	}

}

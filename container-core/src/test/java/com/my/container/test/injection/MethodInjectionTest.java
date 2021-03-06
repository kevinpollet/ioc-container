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

import java.lang.reflect.Field;

import com.my.container.Container;
import com.my.container.binding.FluentBindingProvider;
import com.my.container.test.injection.services.ServiceA;
import com.my.container.test.injection.services.ServiceC;
import com.my.container.test.injection.services.ServiceD;
import com.my.container.test.injection.services.ServiceE;
import com.my.container.test.injection.services.impl.EchoServiceC;
import com.my.container.test.injection.services.impl.LowerEcho;
import com.my.container.test.injection.services.impl.LowerEchoProvider;
import com.my.container.test.injection.services.impl.LowerEchoServiceC;
import com.my.container.test.injection.services.impl.UpperEchoServiceC;
import com.my.container.test.injection.services.impl.methods.MethodNamedServiceA;
import com.my.container.test.injection.services.impl.methods.MethodProviderServiceA;
import com.my.container.test.injection.services.impl.methods.MethodQualifierServiceA;
import com.my.container.test.injection.services.impl.methods.MethodServiceDImpl;
import com.my.container.test.injection.services.impl.methods.MethodServiceEImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kevinpollet
 */
public class MethodInjectionTest {

	@Test
	public void testMethodInjection() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceD.class ).to( MethodServiceDImpl.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceD service = injector.get( ServiceD.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( "Great Method Injection", service.echo( "Great Method Injection" ) );
	}

	@Test
	public void testInjectOverriddenInjectMethod() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceD.class ).to( MethodServiceDImpl.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceD service = injector.get( ServiceD.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( 1, ( (MethodServiceDImpl) service ).getNbCallSetServiceC() );
	}

	@Test
	public void testNoInjectOverriddenInjectMethod() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceD.class ).to( MethodServiceDImpl.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceE service = injector.get( ServiceE.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( 0, ( (MethodServiceEImpl) service ).getNbCallSetServiceC() );
	}

	@Test
	public void testExistingMethodInjection() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceD.class ).to( MethodServiceDImpl.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceD service = new MethodServiceDImpl();
		injector.injectDependencies( service );

		Assert.assertNotNull( service );
		Assert.assertEquals( "Great Method Injection", service.echo( "Great Method Injection" ) );
	}

	@Test
	public void testNamedMethodInjection() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceA.class ).to( MethodNamedServiceA.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
						bind( ServiceC.class ).to( UpperEchoServiceC.class ).named( "upperEchoService" );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceA service = injector.get( ServiceA.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( "ECHO", service.echo( "echo" ) );
	}

	@Test
	public void testQualifiedMethodInjection() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceA.class ).to( MethodQualifierServiceA.class );
						bind( ServiceC.class ).to( UpperEchoServiceC.class ).named( "upperEchoService" );
						bind( ServiceC.class ).to( LowerEchoServiceC.class ).qualifiedBy( LowerEcho.class );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceA service = injector.get( ServiceA.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( "echo", service.echo( "ECHO" ) );
	}

	@Test
	public void testDefaultProviderMethodInjection() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceA.class ).to( MethodProviderServiceA.class );
						bind( ServiceC.class ).to( LowerEchoServiceC.class );
					}
				}
		).buildContainer();

		ServiceA service = injector.get( ServiceA.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( "echo", service.echo( "ECHO" ) );
	}

	@Test
	public void testUserProviderMethodInjection() {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceA.class ).to( MethodProviderServiceA.class );
						bind( ServiceC.class ).toProvider( LowerEchoProvider.class );
					}
				}
		).buildContainer();

		ServiceA service = injector.get( ServiceA.class );

		Assert.assertNotNull( service );
		Assert.assertEquals( "echo", service.echo( "ECHO" ) );
	}

	@Test
	public void testCyclicMethodInjection() throws NoSuchFieldException, IllegalAccessException {
		Container injector = Container.configure().addBindingProvider(
				new FluentBindingProvider() {
					@Override
					public void configureBindings() {
						bind( ServiceD.class ).to( MethodServiceDImpl.class );
						bind( ServiceC.class ).to( EchoServiceC.class );
						bind( ServiceE.class ).to( MethodServiceEImpl.class );
					}
				}
		).buildContainer();

		ServiceD serviceD = injector.get( ServiceD.class );

		//Get depE for serviceD
		Field depE = MethodServiceDImpl.class.getDeclaredField( "serviceE" );
		depE.setAccessible( true );

		//Get depD for serviceE
		Field depD = MethodServiceEImpl.class.getDeclaredField( "serviceD" );
		depD.setAccessible( true );


		Assert.assertNotNull( serviceD );
		Assert.assertEquals( 2, serviceD.add( 1, 1 ) );
		Assert.assertEquals( "Hello", ( (ServiceE) depE.get( serviceD ) ).echo( "Hello" ) );
		Assert.assertSame( serviceD, depD.get( depE.get( serviceD ) ) );

	}

}

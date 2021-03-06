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
package com.my.container.sample.test;

import java.util.Locale;

import com.my.container.Container;
import com.my.container.Configuration;
import com.my.container.sample.GreetingService;
import com.my.container.sample.test.config.LocaleBindingProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Kevin Pollet
 */
public class GreetingServiceTest {

	@Test
	public void testFrenchGreetingService() {
		Locale.setDefault( Locale.FRENCH );

		//Get an Injector instance
		Configuration config = Container.configure();
		Container injector = config.addBindingProvider( new LocaleBindingProvider() ).buildContainer();
		GreetingService service = injector.get( GreetingService.class );

		assertEquals( "Bonjour Kevin !!", service.greet( "Kevin" ) );
	}

	@Test
	public void testEnglishGreetingService() {
		Locale.setDefault( Locale.ENGLISH );

		//Configure Injection
		Configuration config = Container.configure();

		//Get an Injector instance
		Container injector = config.addBindingProvider( new LocaleBindingProvider() ).buildContainer();
		GreetingService service = injector.get( GreetingService.class );

		assertEquals( "Hello Kevin !!", service.greet( "Kevin" ) );
	}

}

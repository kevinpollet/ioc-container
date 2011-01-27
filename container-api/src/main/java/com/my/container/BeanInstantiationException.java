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
package com.my.container;


/**
 * This exception is thrown when the
 * bean factory cannot instantiate the bean.
 *
 * @author Kevin Pollet
 */
public class BeanInstantiationException extends BeanException {

	public BeanInstantiationException(String message) {
		super( message );
	}

	public BeanInstantiationException(final String message, final Throwable cause) {
		super( message, cause );
	}

	public BeanInstantiationException(final Throwable cause) {
		super( cause );
	}
}

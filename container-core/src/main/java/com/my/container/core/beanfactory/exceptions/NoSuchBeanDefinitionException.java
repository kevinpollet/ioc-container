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
package com.my.container.core.beanfactory.exceptions;


/**
 * The exception throws when the bean definition
 * is not found in the core.
 *
 * @author Kevin Pollet
 */
public class NoSuchBeanDefinitionException extends BeanInstantiationException {

	public NoSuchBeanDefinitionException(final String message) {
		super( message );
	}

	public NoSuchBeanDefinitionException(final String message, final Throwable cause) {
		super( message, cause );
	}

	public NoSuchBeanDefinitionException(final Throwable cause) {
		super( cause );
	}
}

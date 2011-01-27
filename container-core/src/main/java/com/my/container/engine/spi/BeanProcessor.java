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
package com.my.container.engine.spi;

/**
 * The bean processor contract. This
 * contract must be implemented by
 * extensions.
 *
 * @author Kevin Pollet
 */
public interface BeanProcessor {

	/**
	 * Check if the bean is processable.
	 *
	 * @param bean the bean to process
	 *
	 * @return true if the bean is processable or false otherwise
	 */
	boolean isProcessable(final Object bean);

	/**
	 * Process the bean in parameter. <b>This method
	 * must never return null.</b>
	 *
	 * @param bean the bean to process
	 * @param <T> the type of the bean
	 *
	 * @return the bean processed
	 *
	 * @throws Exception if an exception occurs during the bean processing
	 */
	<T> T processBean(final T bean) throws Exception;
}

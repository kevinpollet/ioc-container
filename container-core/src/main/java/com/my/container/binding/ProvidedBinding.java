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
package com.my.container.binding;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

/**
 * Create a provided binding. A provided binding
 * is a binding where the bean instance is provided
 * by the provider.
 *
 * @author Kevin Pollet
 */
public class ProvidedBinding<T> extends Binding<T> {

    /**
     * The provider.
     */
    private Class<? extends Provider<T>> provider;


    /**
     * Create a provided binding.
     *
     * @param clazz    the class to be bind
     * @param provider the provider
     */
    public ProvidedBinding(final Class<T> clazz, final Class<? extends Provider<T>> provider) {
        super(clazz, null);
        this.provider = provider;
    }

    /**
     * Create a provided binding.
     *
     * @param clazz    the class to be bind
     * @param provider the provider
     * @param name     the name qualifier
     */
    public ProvidedBinding(final Class<T> clazz, final Class<? extends Provider<T>> provider, final String name) {
        super(clazz, null, name);
        this.provider = provider;
    }

    /**
     * Create a provided binding.
     *
     * @param clazz     the class to be bind
     * @param provider  the provider
     * @param qualifier the qualifier
     */
    public ProvidedBinding(final Class<T> clazz, final Class<? extends Provider<T>> provider, final Class<? extends Annotation> qualifier) {
        super(clazz, null, qualifier);
        this.provider = provider;
    }

    /**
     * Get the provider.
     *
     * @return the provider
     */
    public Class<? extends Provider<T>> getProvider() {
        return this.provider;
    }

    /**
     * Set the provider.
     *
     * @param provider the provider
     */
    public void setProvider(final Class<? extends Provider<T>> provider) {
        this.provider = provider;
    }

}

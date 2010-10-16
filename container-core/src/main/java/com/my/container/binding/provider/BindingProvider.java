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
package com.my.container.binding.provider;

import com.my.container.binding.Binding;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;


/**
 * The BindingProvider class. A Binding
 * provider is responsible to share the
 * bean bindings.
 */
public abstract class BindingProvider {

    /**
     * The bindings list.
     */
    private final List<Binding<?>> bindings;

    /**
     * Create a binding provider.
     */
    protected BindingProvider() {
        this.bindings = new ArrayList<Binding<?>>();
    }

    /**
     * Get the binding provider list.
     */
    public final List<Binding<?>> getBindings() {
        return bindings;
    }

    /**
     * Initiate the binding creation.
     * @param intf the binding interface
     * @return the binding builder
     */
    protected final <T> BasicBindingBuilder<T> bind(final Class<T> intf) {
        return this.new BasicBindingBuilder<T>(new Binding<T>(intf, null));
    }

    /**
     * <p>
     * Configure the binding list provided by this
     * binding provider.
     * </p>
     * <p>
     * To add a binding you can use the following code :
     * <pre>
     * bind(interface.class).to(implementation.class)
     * bind(interface.class).to(implementation.class).named("name")
     * bind(interface.class).to(implementation.class).qualifiedBy(MyQualifier.class)
     * </pre>
     * </p>
     */
    public abstract void configureBindings();

    /*--------- Inner Fluent Binding builder ---------*/

    /**
     * The basic binding builder inner class.
     */
    protected final class BasicBindingBuilder<T> {

        /**
         * The binding to be built.
         */
        private Binding<T> binding;

        /**
         * The BasicBindingBuilder constructor.
         * 
         * @param binding the binding to be build
         */
        private BasicBindingBuilder(final Binding<T> binding) {
            this.binding = binding;
        }

        /**
         * The binding implementation.
         *
         * @param impl the implementation
         */
        public final QualifierBindingBuilder to(final Class<? extends T> impl) {
            this.binding.setImplementation(impl);

            // Binding can be added because a binding
            // is valid with no qualifier.
            BindingProvider.this.bindings.add(binding);

            return  BindingProvider.this.new QualifierBindingBuilder<T>(this.binding);
        }
    }

    /**
     * The qualifier binding builder inner class .
     */
    protected final class QualifierBindingBuilder<T> {

        /**
         * The binding to be build.
         */
        private Binding<T> binding;

        /**
         * Create a QualifierBindingBuilder.
         *
         * @param binding the binding to be built.
         */
        public QualifierBindingBuilder(final Binding<T> binding) {
            this.binding = binding;
        }

        /**
         * The binding Named qualifier.
         * @see javax.inject.Named
         *
         * @param named the Named qualifier binding named
         */
        public final void named(final String named) {
            this.binding.setNamed(named);
        }

        /**
         * The binding qualifier.
         *
         * @param qualifier the class of the qualifier 
         */
        public final void qualifiedBy(final Class<? extends Annotation> qualifier) {
            this.binding.setQualifier(qualifier);
        }

    }

}

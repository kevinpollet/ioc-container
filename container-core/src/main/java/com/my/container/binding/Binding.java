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

import java.lang.annotation.Annotation;

/**
 * The binding definition Class.
 *
 * @author kevinpollet
 */
public class Binding<T> {

    /**
     * The class to be bind.
     */
    private Class<T> clazz;

    /**
     * The binding implementation class.
     */
    private Class<? extends T> impl;

    /**
     * The named qualifier.
     *
     * @see javax.inject.Named
     */
    private String name;

    /**
     * The qualifier. A qualifier is an
     * annotation annotated with @Qualifier.
     *
     * @see javax.inject.Qualifier
     */
    private Class<? extends Annotation> qualifier;

    /**
     * Create a simple binding where the class to be bind
     * is equal to the class implementation.
     *
     * @param clazz the class to be bind
     */
    public Binding(final Class<T> clazz) {
        this(clazz, clazz);
    }

    /**
     * Create a binding.
     *
     * @param clazz the binding class
     * @param impl  the binding implementation
     */
    public Binding(final Class<T> clazz, final Class<? extends T> impl) {
        this.clazz = clazz;
        this.impl = impl;
        this.name = null;
        this.qualifier = null;
    }

    /**
     * <p>
     * Create a named binding.
     * </p>
     * <p>
     * A Named binding is a binding who can be injected if the
     * injection is qualified by @Name like :
     * <br/>
     * <br/>
     * <pre>
     * public class Sample {
     *
     *     &#64;Inject &#64;Name("name")
     *     private Dependency dependency;
     *
     *     &#64;Inject
     *     public Sample(&#64;Name("name") Dependency dependency) {
     *      //...
     *     }
     *
     *     &#64;Inject
     *     public void sampleMethod(&#64;Name("name") Dependency dependency) {
     *      //...
     *     }
     *
     *  }
     * </pre>
     * </p>
     *
     * @param clazz the binding class
     * @param impl  the binding class implementation
     * @param name  the name qualifier of this binding
     * @see javax.inject.Named
     */
    public Binding(final Class<T> clazz, final Class<? extends T> impl, final String name) {
        this.clazz = clazz;
        this.impl = impl;
        this.name = name;
        this.qualifier = null;
    }

    /**
     * <p>
     * Create a qualified binding.
     * </p>
     * <p>
     * A Qualified binding is a binding who can be injected if the
     * injection is qualified by the the given qualifier. For example
     * with the qualifier @Foo :
     * <br/>
     * <br/>
     * <pre>
     * <code>public class Sample {
     *
     *     &#64;Inject &#64;Foo
     *     private Dependency dependency;
     *
     *     &#64;Inject
     *     public Sample(&#64;Foo Dependency dependency) {
     *      //...
     *     }
     *
     *     &#64;Inject
     *     public void sampleMethod(&#64;Foo Dependency dependency) {
     *      //...
     *     }
     *
     *  }</code>
     * </pre>
     * </p>
     *
     * @param clazz     the binding class
     * @param impl      the binding implementation
     * @param qualifier the qualifier of the binding
     * @see javax.inject.Qualifier
     */
    public Binding(final Class<T> clazz, final Class<? extends T> impl, final Class<? extends Annotation> qualifier) {
        this.clazz = clazz;
        this.impl = impl;
        this.name = null;
        this.qualifier = qualifier;
    }


    /**
     * Get the qualifier for this binding.
     *
     * @return the qualifier
     */
    public Class<? extends Annotation> getQualifier() {
        return this.qualifier;
    }

    /**
     * Get the binding class.
     *
     * @return the biding class
     */
    public Class<T> getClazz() {
        return this.clazz;
    }

    /**
     * Get the binding implementation class.
     *
     * @return the implementation class
     */
    public Class<? extends T> getImplementation() {
        return this.impl;
    }

    /**
     * Get the binding Named qualifier value.
     *
     * @return the value of the name qualifier or null if none
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the binding class.
     *
     * @param clazz the binding class
     */
    public void setClazz(final Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Set the binding implementation class.
     *
     * @param impl the implementation class
     */
    public void setImplementation(final Class<? extends T> impl) {
        this.impl = impl;
    }

    /**
     * Set the binding name qualifier value
     *
     * @param name the name of the name qualifier value
     */
    public void setNamed(final String name) {
        this.name = name;
    }

    /**
     * Set the binding qualifier.
     *
     * @param qualifier the qualifier of the binding or null if none
     */
    public void setQualifier(final Class<? extends Annotation> qualifier) {
        this.qualifier = qualifier;
    }

}

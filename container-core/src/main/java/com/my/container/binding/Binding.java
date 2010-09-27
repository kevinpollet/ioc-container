package com.my.container.binding;

import java.lang.annotation.Annotation;

/**
 * The binding definition Class.
 *
 * @author kevinpollet
 */
public class Binding<T> {

    /**
     * The contract of the binding.
     */
    private Class<T> intf;

    /**
     * The implementation of the binding.
     */
    private Class<? extends T> impl;

    /**
     * The named qualifier.
     * @see javax.inject.Named
     */
    private String name;

    /**
     * The customs qualifier. A qualifier is
     * an annotation annotated with @Qualifier.
     * @see javax.inject.Qualifier
     */
    private Class<? extends Annotation> qualifier;

    /**
     * Create a binding.
     *
     * @param intf the binding interface
     * @param impl the binding implementation
     */
    public Binding(final Class<T> intf, final Class<? extends T> impl) {
        this.intf = intf;
        this.impl = impl;
        this.name = null;
        this.qualifier = null;
    }

    /**
     * Create a named binding. A Named binding is used
     * when the contract is implemented by multiple class.
     * @see javax.inject.Named
     *
     * @param intf the binding interface
     * @param impl the binding implementation
     * @param name the name of the binding
     */
    public Binding(final Class<T> intf, final Class<? extends T> impl, final String name) {
        this.intf = intf;
        this.impl = impl;
        this.name = name;
        this.qualifier = null;
    }

    /**
     * Create a custom qualified binding. A qualified binding is used
     * when the contract is implemented by multiple class.
     * @see javax.inject.Qualifier
     *
     * @param intf the binding interface
     * @param impl the binding implementation
     * @param qualifier the qualifier of the binding
     */
    public Binding(final Class<T> intf, final Class<? extends T> impl, final Class<? extends Annotation> qualifier) {
        this.intf = intf;
        this.impl = impl;
        this.name = null;
        this.qualifier = qualifier;
    }


    /**
     * Get the qualifier for this binding.
     * @return the qualifier
     */
    public Class<? extends Annotation> getQualifier() {
       return this.qualifier; 
    }

    /**
     * Get the binding interface class.
     *
     * @return the interface class
     */
    public Class<T> getInterface() {
        return this.intf;
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
     * Get the binding Named qualifier.
     *
     * @return the name of the binding
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the binding interface class.
     *
     * @param intf the binding interface class
     */
    public void setInterface(final Class<T> intf) {
        this.intf = intf;
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
     * Set the binding named
     *
     * @param name the name of the binding
     */
    public void setNamed(final String name) {
        this.name = name;
    }

    /**
     * Set the binding qualifier.
     * @param qualifier the qualifier
     */
    public void setQualifier(final Class<? extends Annotation> qualifier) {
        this.qualifier = qualifier;
    }

}

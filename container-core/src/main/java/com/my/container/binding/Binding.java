package com.my.container.binding;

/**
 * The binding definition Class.
 *
 * @author kevinpollet
 */
public class Binding<T> {

    private Class<T> intf;

    private Class<? extends T> impl;

    private String name;

    /**
     * Create a binding.
     *
     * @param intf the binding interface
     * @param impl the binding implementation
     */
    public Binding(final Class<T> intf, final Class<? extends T> impl) {
        this(intf, impl, null);
    }

    /**
     * Create a qualify binding. A Named binding is used
     * when the contract is implemented by multiple class.
     * This Named is the Qualifier.
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
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object to) {
        boolean eq = false;

        if (to == this) {
            eq = true;
        } else if (to instanceof Binding) {
            Binding toBinding = (Binding) to;

            if (toBinding.getInterface() != null) {
              eq = toBinding.getInterface().equals(this.intf);
              if (toBinding.getName() != null) {
                eq = eq && toBinding.getName().equals(this.name);
              }
            }
            
        }

        return eq;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;

        if (this.intf != null) {
            hash += this.intf.hashCode();
            
            if (this.name != null) {
                hash += this.name.hashCode();
            }
        }

        return hash;
    }
    
}

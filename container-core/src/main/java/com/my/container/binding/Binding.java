package com.my.container.binding;

/**
 * The binding class.
 * 
 * @author kevinpollet
 */
public class Binding {

    private Class<?> intf;
    private Class<?> impl;

    /**
     * Create a binding.
     *
     * @param intf the binding interface
     * @param impl the binding implementation
     */
    public Binding(final Class<?> intf, final Class<?> impl) {
        this.intf = intf;
        this.impl = impl;
    }

    /**
     * Get the binding interface class.
     * @return the interface class
     */
    public Class<?> getInterface() {
        return this.intf;
    }

    /**
     * Get the binding implementation class.
     * @return the implementation class
     */
    public Class<?> getImplementation() {
        return this.impl;
    }

    /**
     * Set the binding interface class.
     * @param intf the binding interface class
     */
    public void setInterface(final Class<?> intf) {
        this.intf = intf;
    }

    /**
     * Set the binding implementation class.
     * @param impl the implementation class
     */
    public void setImplementation(final Class<?> impl) {
        this.impl = impl;
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
            Binding b = (Binding) to;

            if (b.getInterface() != null) {
                eq = this.intf.equals(b.getInterface());
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
            hash = this.intf.hashCode();
        }

        return hash;
    }
}

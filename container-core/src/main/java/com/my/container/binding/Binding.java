package com.my.container.binding;

public class Binding {

    /**
     * The binding interface.
     */
    private Class<?> intf;

    /**
     * The binding implementation.
     */
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

    public Class<?> getInterface() {
        return this.intf;
    }

    public Class<?> getImplementation() {
        return this.impl;
    }

    public void setInterface(final Class<?> intf) {
        this.intf = intf;
    }

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

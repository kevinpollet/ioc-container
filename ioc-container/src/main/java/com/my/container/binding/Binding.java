package com.my.container.binding;


public class Binding {

    private Class<?> intf;
    private Class<?> impl;

    public Binding() {
        this(null, null);
    }

    public <T> Binding(final Class<T> intf, final Class<? extends T> impl) {
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

        if (to == this) return true;
        else if (to instanceof Binding && ((Binding) to).getInterface() != null) {
            return ((Binding) to).getInterface().equals(this.intf);
        }

        return false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.intf.hashCode();
    }
}

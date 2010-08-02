package com.my.container.binding.provider;

import com.my.container.binding.Binding;

import java.util.ArrayList;
import java.util.List;


public abstract class BindingProvider {

    private List<Binding> bindings;

    public BindingProvider() {
        this.bindings = new ArrayList<Binding>();
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public <T> void addBinding(final Class<T> intf, final Class<? extends T> impl) {
        this.bindings.add(new Binding(intf, impl));
    }

    /**
     * Configure the binding list.
     */
    public abstract void configureBindings();

}

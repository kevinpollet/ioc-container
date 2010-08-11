package com.my.container.binding.provider;

import com.my.container.binding.Binding;

import java.util.ArrayList;
import java.util.List;


//TODO callback method invocation
public abstract class BindingProvider {

    private List<Binding> bindings;

    public BindingProvider() {
        this.bindings = new ArrayList<Binding>();
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public BindingBuilder bind(final Class<?> intf) {
        Binding binding = new Binding(intf, null);
        this.bindings.add(binding);

        //Link it to an implementation
        return new BindingBuilder(binding);
    }

    /**
     * Configure the binding list.
     * Add the binding.
     *
     * USE : bind(interface.class).to(implementation.class)
     */
    public abstract void configureBindings();
}

package com.my.container.binding.provider;

import com.my.container.binding.Binding;

import java.util.ArrayList;
import java.util.List;


/**
 * The BindingProvider class. A Binding
 * provider is responsible to share the
 * class binding.
 */
public abstract class BindingProvider {

    private List<Binding> bindings;

    /**
     * Create a binding provider.
     */
    public BindingProvider() {
        this.bindings = new ArrayList<Binding>();
    }

    /**
     * Get the binding provider
     * list.
     */
    public List<Binding> getBindings() {
        return bindings;
    }

    /**
     * Initiate the binding creation.
     * @param intf the binding interface
     * @return the binding builder
     */
    protected InnerBindingBuilder bind(final Class<?> intf) {
        return this.new InnerBindingBuilder(intf);
    }

    /**
     * Configure the binding list.
     * 
     * To add a binding :
     * USE : bind(interface.class).to(implementation.class)
     */
    public abstract void configureBindings();


    /**
     * The binding builder inner class
     */
    protected class InnerBindingBuilder {

        private Class<?> intf;

        private InnerBindingBuilder(final Class<?> intf) {
            this.intf = intf;
        }

        /**
         * The binding implementation
         * @param impl the implementation
         */
        public void to(final Class<?> impl) {
            bindings.add(new Binding(this.intf, impl));
        }
    }

}

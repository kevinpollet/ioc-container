package com.my.container.binding.provider;

import com.my.container.binding.Binding;

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
    private List<Binding> bindings;

    /**
     * Create a binding provider.
     */
    public BindingProvider() {
        this.bindings = new ArrayList<Binding>();
    }

    /**
     * Get the binding provider list.
     */
    public final List<Binding> getBindings() {
        return bindings;
    }

    /**
     * Initiate the binding creation.
     * @param intf the binding interface
     * @return the binding builder
     */
    protected final <T> InnerBindingBuilder<T> bind(final Class<T> intf) {
        return this.new InnerBindingBuilder<T>(intf);
    }

    /**
     * Configure the binding list.
     * <br/>
     * <p>To add a binding use the following code :
     * {@code bind(interface.class).to(implementation.class)}</p>
     */
    public abstract void configureBindings();


    /**
     * The binding builder inner class
     */
    protected final class InnerBindingBuilder<T> {

        /**
         * The binding interface class.
         */
        private Class<T> intf;

        /**
         * The InnerBindingBuilder constructor.
         * @param intf the binding interface
         */
        private InnerBindingBuilder(final Class<T> intf) {
            this.intf = intf;
        }

        /**
         * The binding implementation
         * @param impl the implementation
         */
        public final void to(final Class<? extends T> impl) {
            bindings.add(new Binding<T>(this.intf, impl));
        }
    }

}

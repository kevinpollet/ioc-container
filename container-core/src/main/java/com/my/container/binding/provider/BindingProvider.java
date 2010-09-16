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
    protected final <T> BasicBindingBuilder<T> bind(final Class<T> intf) {
        return this.new BasicBindingBuilder(new Binding(intf, null));
    }

    /**
     * Configure the binding list.
     * <br/>
     * <p>To add a binding use the following code :
     * {@code bind(interface.class).to(implementation.class)}</p>
     */
    public abstract void configureBindings();


    /**
     * The basic binding builder inner class.
     */
    protected final class BasicBindingBuilder<T> {

        /**
         * The binding to be built.
         */
        private Binding binding;

        /**
         * The BasicBindingBuilder constructor.
         * 
         * @param binding the binding to be build
         */
        private BasicBindingBuilder(final Binding binding) {
            this.binding = binding;
        }

        /**
         * The binding implementation.
         *
         * @param impl the implementation
         */
        public final QualifierBindingBuilder to(final Class<? extends T> impl) {
            this.binding.setImplementation(impl);

            // Binding can be added because a binding
            // is valid with no qualifier.
            BindingProvider.this.bindings.add(binding);

            return  BindingProvider.this.new QualifierBindingBuilder(this.binding);
        }
    }

    /**
     * The qualifier binding builder inner class .
     */
    protected final class QualifierBindingBuilder {

        /**
         * The binding to be build.
         */
        private Binding binding;

        /**
         * Create a QualifierBindingBuilder.
         *
         * @param binding the binding to be built.
         */
        public QualifierBindingBuilder(final Binding binding) {
            this.binding = binding;
        }

        /**
         * The binding Named qualifier.
         * @see javax.inject.Named
         *
         * @param named the Named qualifier binding named
         */
        public final void named(final String named) {
            this.binding.setNamed(named);
        }

    }

}

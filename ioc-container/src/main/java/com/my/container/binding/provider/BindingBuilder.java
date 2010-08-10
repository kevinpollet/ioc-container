package com.my.container.binding.provider;

import com.my.container.binding.Binding;

import java.util.List;

public class BindingBuilder {

    private Binding binding;

    public BindingBuilder(final Binding binding) {
        this.binding = binding;
    }

    public Binding to(final Class<?> impl) {
        this.binding.setImplementation(impl);
        return this.binding;
    }
}

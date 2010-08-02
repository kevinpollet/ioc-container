package com.my.container.services;

import com.my.container.binding.provider.BindingProvider;

public class ServiceBinder extends BindingProvider {

    @Override
    public void configureBindings() {
        this.addBinding(Service.class, ServiceImpl.class);
    }
}

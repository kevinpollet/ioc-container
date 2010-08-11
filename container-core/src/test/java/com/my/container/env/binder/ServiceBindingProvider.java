package com.my.container.env.binder;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.env.services.HelloService;
import com.my.container.env.services.HelloServiceImpl;

public class ServiceBindingProvider extends BindingProvider {

    @Override
    public void configureBindings() {
        bind(HelloService.class).to(HelloServiceImpl.class);
    }
}

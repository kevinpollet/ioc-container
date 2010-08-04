package com.my.container.test.callbacks.binder;

import com.my.container.binding.provider.BindingProvider;
import com.my.container.test.callbacks.services.Service;
import com.my.container.test.callbacks.services.ServiceImpl;

public class BindingProviderTest extends BindingProvider {

    @Override
    public void configureBindings() {
        bind(Service.class).to(ServiceImpl.class);
    }
}

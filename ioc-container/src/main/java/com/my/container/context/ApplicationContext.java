package com.my.container.context;

import com.my.container.binding.Binding;
import com.my.container.binding.provider.BindingProvider;
import com.my.container.exceptions.beanfactory.BeanInstanciationException;
import com.my.container.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApplicationContext implements Context {

    private Map<Binding, Binding> bindings;
    private Map<Binding, Object> singletons;

    public ApplicationContext(final BindingProvider binder) {
        this.bindings = new HashMap<Binding, Binding>();
        this.singletons = new HashMap<Binding, Object>();

        binder.configureBindings();

        for (Binding b : binder.getBindings()) {
            this.bindings.put(b, b);
        }
    }

    public <T> T getBean(final Class<T> clazz) {

        T newInstance = null;

        Binding key = new Binding(clazz, null);
        Binding binding = this.bindings.get(key);

        if (binding != null) {

            //Check if binding is singleton
            if (binding.getImplementation().isAnnotationPresent(Singleton.class)) {
                newInstance = (T) this.singletons.get(binding);
            }

            //Create new instance
            if (newInstance == null) {

                try {

                    newInstance = (T) binding.getImplementation().newInstance();

                    //Call LifeCycle callback
                    List<Method> methodList = ReflectionUtil.getMethodAnnotatedWith(newInstance.getClass(), PostConstruct.class);
                    if (methodList.size() == 1) {

                        try {

                            if (methodList.get(0).getModifiers() == Modifier.PRIVATE) {
                                methodList.get(0).setAccessible(true);
                            }

                            methodList.get(0).invoke(newInstance, null);

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    

                    if (binding.getImplementation().isAnnotationPresent(Singleton.class)) {
                        this.singletons.put(binding, newInstance);
                    }

                } catch (InstantiationException e) {
                    throw new BeanInstanciationException(e.getMessage());
                } catch (IllegalAccessException e) {
                    throw new BeanInstanciationException(e.getMessage());
                }
            }
        }

        return newInstance;
    }

}

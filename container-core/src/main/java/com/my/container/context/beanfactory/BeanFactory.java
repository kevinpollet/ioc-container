package com.my.container.context.beanfactory;

import com.my.container.binding.Binding;
import com.my.container.exceptions.beanfactory.BeanClassNotFoundException;
import com.my.container.exceptions.beanfactory.BeanInstanciationException;
import com.my.container.exceptions.callback.CallbackInvocationException;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BeanFactory {

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Binding, Binding> bindings;

    private List<Object> prototypesBean;
    private Map<Binding, Object> singletonsBean;

    public BeanFactory(final List<Binding> list) {

        //Populate bindings list
        this.bindings = new HashMap<Binding, Binding>();
        if (list != null) {
            for (Binding b : list) {
                this.bindings.put(b, b);
            }
        }

        //Initialize bean instance holder        
        this.prototypesBean = new ArrayList<Object>();
        this.singletonsBean = new HashMap<Binding, Object>();
    }

    public <T> T getBean(final Class<T> clazz) {

        T beanInstance = null;
        Binding binding = bindings.get(new Binding(clazz, null));

        if (binding == null || binding.getImplementation() == null) {

            throw new BeanClassNotFoundException("Binding not found");

        } else {

            boolean isSingleton = binding.getImplementation().isAnnotationPresent(Singleton.class);

            if (isSingleton && this.singletonsBean.containsKey(binding)) {

                beanInstance = (T) this.singletonsBean.get(binding);

            } else {

                //TODO moche
                try {

                    beanInstance = (T) binding.getImplementation().newInstance();


                } catch (InstantiationException e) {
                    throw new BeanInstanciationException(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BeanInstanciationException(e.getMessage(), e);
                }

                try {

                    //Call PostConstruct TODO call PostConstruct after injection
                    ReflectionHelper.callDeclaredMethodWith(PostConstruct.class, beanInstance);

                    if (isSingleton) {
                        this.singletonsBean.put(binding, beanInstance);
                    } else {
                        this.prototypesBean.add(beanInstance);
                    }

                } catch (InvocationTargetException e) {
                    throw new CallbackInvocationException(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new CallbackInvocationException(e.getMessage(), e);
                }
            }

        }


        return beanInstance;
    }

    public void removeAllBeansReferences() {

        try {

            //Call PreDestroy methods on prototypes bean
            for (Object o : prototypesBean) {
                ReflectionHelper.callDeclaredMethodWith(PreDestroy.class, o);
            }

            //Call PreDestroy methods on singletons bean
            Set<Map.Entry<Binding, Object>> set = this.singletonsBean.entrySet();
            for (Map.Entry<Binding, Object> e : set) {
                ReflectionHelper.callDeclaredMethodWith(PreDestroy.class, e.getValue());
            }

        } catch (InvocationTargetException e) {
            throw new CallbackInvocationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new CallbackInvocationException(e.getMessage(), e);
        }
        
    }

}

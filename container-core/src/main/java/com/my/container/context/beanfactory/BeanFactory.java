package com.my.container.context.beanfactory;

import com.my.container.annotations.interceptors.Interceptors;
import com.my.container.binding.Binding;
import com.my.container.context.beanfactory.proxy.InterceptorInvocationHandler;
import com.my.container.exceptions.beanfactory.BeanClassNotFoundException;
import com.my.container.exceptions.beanfactory.BeanInstantiationException;
import com.my.container.exceptions.callback.CallbackInvocationException;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO interceptor verification
//TODO one instance interceptor for all prototypes ???
//TODO callbacks method verification
//TODO factorize code

/**
 * The factory of bean class
 *
 * @author kevinpollet
 */
public class BeanFactory {

    private final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Binding, Binding> bindings;
    private List<Object> prototypesBean;
    private Map<Binding, Object> singletonsBean;

    /**
     * Create a bean factory
     *
     * @param list the binding list
     */
    public BeanFactory(final List<Binding> list) {

        //Populate bindings list
        this.bindings = new HashMap<Binding, Binding>();
        if (list != null) {
            for (Binding b : list) {
                this.bindings.put(b, b);
            }
        }

        //Initialize beans instance holder for prototypes and singleton        
        this.prototypesBean = new ArrayList<Object>();
        this.singletonsBean = new HashMap<Binding, Object>();
    }

    /**
     * Get a bean defined by this class (generally the
     * bean interface).
     *
     * @param clazz the bean class
     * @return the bean instance
     */
    public <T> T getBean(final Class<T> clazz) {
        T beanInstance = null;
        Binding binding = bindings.get(new Binding(clazz, null));

        if (binding == null || binding.getImplementation() == null) {
            throw new BeanClassNotFoundException("Binding not found or not valid");
        } else {

            boolean isSingleton = binding.getImplementation().isAnnotationPresent(Singleton.class);

            if (isSingleton && this.singletonsBean.containsKey(binding)) {

                beanInstance = (T) this.singletonsBean.get(binding);

            } else {

                try {

                    beanInstance = (T) this.makeBeanInstance(binding.getImplementation());

                    //Call @PostConstruct method if one
                    if (Proxy.isProxyClass(beanInstance.getClass()) && Proxy.getInvocationHandler(beanInstance) instanceof InterceptorInvocationHandler) {
                        InterceptorInvocationHandler handler = (InterceptorInvocationHandler) Proxy.getInvocationHandler(beanInstance);
                        ReflectionHelper.callDeclaredMethodWith(PostConstruct.class, handler.getProxiedInstance());
                    } else {
                        ReflectionHelper.callDeclaredMethodWith(PostConstruct.class, beanInstance);
                    }

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

            //Create on list for all beans
            List<Object> allBeans = new ArrayList<Object>(){{
                    addAll(prototypesBean);
                    addAll(singletonsBean.values());
            }};

            //Call PreDestroy methods bean
            for (Object o : allBeans) {
                if (Proxy.isProxyClass(o.getClass()) && Proxy.getInvocationHandler(o) instanceof InterceptorInvocationHandler) {
                    InterceptorInvocationHandler handler = (InterceptorInvocationHandler) Proxy.getInvocationHandler(o);
                    ReflectionHelper.callDeclaredMethodWith(PreDestroy.class, handler.getProxiedInstance());
                } else {
                    ReflectionHelper.callDeclaredMethodWith(PreDestroy.class, o);
                }
            }

            //Remove references
            allBeans.clear();
            this.prototypesBean.clear();
            this.singletonsBean.clear();

        } catch (InvocationTargetException e) {
            throw new CallbackInvocationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new CallbackInvocationException(e.getMessage(), e);
        }

    }

    /**
     * Create an instance of the given clazz.
     *
     * @param clazz the class
     * @return the instance
     * @throws BeanInstantiationException if an error occur during class instantiation
     *                                    there is no empty constructor, or constructor is not accessible
     */
    private Object makeBeanInstance(final Class<?> clazz) {
        Object instance = null;

        try {

            instance = clazz.newInstance();

            if (clazz.isAnnotationPresent(Interceptors.class)) {
                //Create interceptor class
                Class<?>[] interceptorsClass = clazz.getAnnotation(Interceptors.class).value();
                List<Object> interceptors = new ArrayList<Object>();

                for (Class<?> c : interceptorsClass) {
                    interceptors.add(c.newInstance());
                }

                instance = Proxy.newProxyInstance(clazz.getClassLoader(),
                                                  clazz.getInterfaces(),
                                                  new InterceptorInvocationHandler(instance, interceptors.toArray()));
            }

        } catch (InstantiationException e) {
            throw new BeanInstantiationException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException(e.getMessage(), e);
        }

        return instance;
    }


}

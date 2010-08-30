package com.my.container.context.beanfactory;

import com.my.container.annotations.interceptors.Interceptors;
import com.my.container.binding.Binding;
import com.my.container.context.beanfactory.exceptions.BeanDependencyInjectionException;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.context.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.context.beanfactory.proxy.AbstractBeanInvocationHandler;
import com.my.container.context.beanfactory.proxy.InterceptorInvocationHandler;
import com.my.container.context.beanfactory.exceptions.CallbackInvocationException;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO interceptor verification
//TODO one instance interceptor for all prototypes ???
//TODO callbacks method verification
//TODO add @Inject constructor, methods, fields injection
//TODO add @Inject static and instance member
//TODO circular dependencies
//TODO cyclic and dependencies
//TODO postconstruct injection can be incompleted

/**
 * The factory of bean class
 *
 * @author kevinpollet
 */
public class BeanFactory {

    private final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Binding, Binding> bindings;

    private Map<Class<?>, Object> markMap;

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

        //Cyclic dependencies map
        this.markMap = new HashMap<Class<?>, Object>();
    }

    /**
     * Get a bean defined by this class (generally the
     * bean interface).
     *
     * @param clazz the bean class
     * @return the bean instance
     * @throws IllegalArgumentException if the bean contract clazz is null
     * @throws NoSuchBeanDefinitionException if there is no binding for this bean contract class
     */
    public <T> T getBean(final Class<T> clazz) {
        T beanInstance = null;

        if (clazz == null) {
            throw new IllegalArgumentException("The parameter clazz cannot be null");
        } else {

            Binding binding = bindings.get(new Binding(clazz, null));

            if (binding == null || binding.getImplementation() == null) {
                throw new NoSuchBeanDefinitionException("The binding for the class " + clazz.getName()+ " is not found or not valid");
            } else {

                 Class<?> implClass = binding.getImplementation() ;
                 boolean isSingleton = implClass.isAnnotationPresent(Singleton.class);

                 if (isSingleton && this.singletonsBean.containsKey(binding)) {
                    beanInstance = (T) this.singletonsBean.get(binding);
                 } else if (this.markMap.containsKey(implClass)) {
                    beanInstance = (T) this.markMap.get(implClass);
                 } else {

                    try {

                        beanInstance = (T) implClass.newInstance();

                        if (implClass.isAnnotationPresent(Interceptors.class)) {

                            Class<?>[] interceptorsClass = implClass.getAnnotation(Interceptors.class).value();
                            List<Object> interceptors = new ArrayList<Object>();

                            for (Class<?> c : interceptorsClass) {
                                interceptors.add(c.newInstance());
                            }

                            beanInstance = (T) Proxy.newProxyInstance(implClass.getClassLoader(),
                                                                      implClass.getInterfaces(),
                                                                      new InterceptorInvocationHandler(beanInstance, interceptors.toArray()));

                        }

                    } catch (InstantiationException e) {
                        throw new BeanInstantiationException(e);
                    } catch (IllegalAccessException e) {
                        throw new BeanInstantiationException(e);
                    }

                    // Inject @Inject annotated fields
                    this.injectAnnotatedFields(beanInstance, implClass);


                    // Injection is done call PostConstruct
                    try {

                        if (Proxy.isProxyClass(beanInstance.getClass())) {

                            if (Proxy.getInvocationHandler(beanInstance) instanceof AbstractBeanInvocationHandler) {
                                AbstractBeanInvocationHandler handler = (AbstractBeanInvocationHandler) Proxy.getInvocationHandler(beanInstance);
                                ReflectionHelper.invokeDeclaredMethodWith(PostConstruct.class, handler.getProxiedInstance());
                            }

                        } else {

                            ReflectionHelper.invokeDeclaredMethodWith(PostConstruct.class, beanInstance);

                        }

                    } catch (InvocationTargetException e) {
                         throw new CallbackInvocationException(e);
                    } catch (IllegalAccessException e) {
                        throw new CallbackInvocationException(e);
                    }


                    // Holds singleton and Prototype beans
                    if (implClass.isAnnotationPresent(Singleton.class)) {
                        this.singletonsBean.put(binding, beanInstance);
                    } else {
                        this.prototypesBean.add(beanInstance);
                    }

                }

            }

        }


        return beanInstance;
    }

    /**
     * This method remove all beans references
     * memorised by the singleton for PreDestroy
     * CallBack and Singleton scope.
     */
    public void removeAllBeansReferences() {

        try {

            //Create on list for all beans
            List<Object> allBeans = new ArrayList<Object>() {{
                addAll(prototypesBean);
                addAll(singletonsBean.values());
            }};

            for (Object bean : allBeans) {
                if (Proxy.isProxyClass(bean.getClass()) && Proxy.getInvocationHandler(bean) instanceof AbstractBeanInvocationHandler) {
                    AbstractBeanInvocationHandler handler = (AbstractBeanInvocationHandler) Proxy.getInvocationHandler(bean);
                    ReflectionHelper.invokeDeclaredMethodWith(PreDestroy.class, handler.getProxiedInstance());
                } else {
                    ReflectionHelper.invokeDeclaredMethodWith(PreDestroy.class, bean);
                }
            }

            allBeans.clear();
            this.prototypesBean.clear();
            this.singletonsBean.clear();

        } catch (InvocationTargetException e) {
            throw new CallbackInvocationException(e);
        } catch (IllegalAccessException e) {
            throw new CallbackInvocationException(e);
        }
    }

    /**
     * Inject dependency in the instance. The SuperClass are injected
     * first.
     *
     * @param instance where inject dependencies annotated by @Inject
     * @param clazz    the class who contains the field definition who can be injected
     */
    private void injectAnnotatedFields(final Object instance, final Class<?> clazz) {
        this.logger.debug("Inject fields of class {}", clazz.getName());

        // If class have a SuperClass inject it before
        if (clazz.getSuperclass() != null) {
            this.injectAnnotatedFields(instance, clazz.getSuperclass());
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            // The class have a dependency
            if (field.isAnnotationPresent(Inject.class)) {

                // Inject final field is not valid
                if (Modifier.isFinal(field.getModifiers())) {
                   throw new BeanDependencyInjectionException("Final field " + field.getName() + " in class " + field.getDeclaringClass().getName() + " cannot be injected");
                } else {

                    //If this class is not already marked, mark it
                    if (!this.markMap.containsKey(instance.getClass())) {
                        this.markMap.put(instance.getClass(), instance);
                    }

                    // Field is not accessible
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    Object dependency = this.getBean(field.getType());

                    try {

                        field.set(instance, dependency);

                    } catch (IllegalAccessException ex) {
                        throw new BeanDependencyInjectionException("The field " + field.getName() + "is not accessible and cannot be injected");
                    }

                }

            }

        }

        // All field are injected remove it from the map
        if (this.markMap.containsKey(instance.getClass())) {
            this.markMap.remove(instance.getClass());
        }

    }

}

package com.my.container.context.beanfactory;

import com.my.container.interceptors.annotations.AroundInvoke;
import com.my.container.interceptors.annotations.Interceptors;
import com.my.container.binding.Binding;
import com.my.container.context.beanfactory.exceptions.BeanDependencyInjectionException;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.context.beanfactory.exceptions.CallbackInvocationException;
import com.my.container.context.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.interceptors.BeanInterceptorInvocationHandler;
import com.my.container.context.beanfactory.proxy.ProxyHelper;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO interceptor verification
//TODO one instance interceptor for all prototypes ???
//TODO callbacks method verification
//TODO constructor cyclic dependencies
//TODO refactor interceptor
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
     * Bean Factory constructor.
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
     * Get a bean defined by this class.
     * This class mut be the interface of the binding.
     *
     * @param clazz the bean class
     * @return the bean instance
     * @throws NoSuchBeanDefinitionException if there is no binding implementation for this bean contract
     * @throws IllegalArgumentException      if the clazz argument is null
     */
    public <T> T getBean(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        }

        List<Object> newBeansCreated = new ArrayList<Object>();

        T createdInstance = this.makeInstance(clazz, new HashMap<Class<?>, Object>(), newBeansCreated);

        // All objects are injected call the PostContruct methods in the right order
        try {

            for (Object instance : newBeansCreated) {
                ReflectionHelper.invokeDeclaredMethodWith(PostConstruct.class, ProxyHelper.getTargetObject(instance));
            }

        } catch (InvocationTargetException e) {
            throw new CallbackInvocationException(e);
        } catch (IllegalAccessException e) {
            throw new CallbackInvocationException(e);
        }

        return createdInstance;
    }

    /**
     * This method create a bean from it's interface. Check the
     *
     * @param clazz           the interface class
     * @param markMap         the map used to resolve cyclic dependencies
     * @param newBeansCreated the list of the new beans created (the bean dependencies)
     * @return the newly created instance
     * @throws NoSuchBeanDefinitionException if there is no binding implementation for this bean contract
     */
    private <T> T makeInstance(final Class<T> clazz, final Map<Class<?>, Object> markMap, final List<Object> newBeansCreated) {
        T beanInstance = null;
        Binding binding = bindings.get(new Binding(clazz, null));

        if (binding == null || binding.getImplementation() == null) {
            throw new NoSuchBeanDefinitionException("The binding for the class " + clazz.getSimpleName() + " is not found or not valid");
        }

        Class<?> implClass = binding.getImplementation();
        boolean isSingleton = implClass.isAnnotationPresent(Singleton.class);

        if (isSingleton && this.singletonsBean.containsKey(binding)) {
            beanInstance = (T) this.singletonsBean.get(binding);
        } else if (markMap.containsKey(implClass)) {
            beanInstance = (T) markMap.get(implClass);
            if (beanInstance == null) {
                throw new BeanDependencyInjectionException("Cyclic dependency injection is not authorized in constructor");
            }
        } else {

            try {


                // Inject constructors only first annotated will be used
                // or default constructor if none have been annotated with
                //@Inject.

                Constructor<?>[] constructors = implClass.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {

                     if (constructor.isAnnotationPresent(Inject.class) && !Modifier.isPrivate(constructor.getModifiers())) {

                         Class<?>[] types = constructor.getParameterTypes();
                         if(types.length > 0) {
                            // Mark constructor because he have dependency with null
                            markMap.put(implClass, null);
                         }

                         List<Object> params = new ArrayList<Object>();
                         for (Class<?> typeClass : types) {
                             params.add(this.makeInstance(typeClass, markMap, newBeansCreated));
                         }

                         beanInstance = (T) constructor.newInstance(params.toArray());
                         break;
                     }

                }

                // No annotated constructor have been found call default constructor
                if (beanInstance == null) {
                    beanInstance = (T) implClass.newInstance();
                }

                // Check if class have to be proxied for interceptors
                List<Method> methods = ReflectionHelper.getDeclaredMethodsAnnotatedWith(AroundInvoke.class, implClass);
                if (implClass.isAnnotationPresent(Interceptors.class) || !methods.isEmpty()) {

                    Method aroundMethod = null;
                    List<Object> interceptorsList = new ArrayList<Object>();

                    Interceptors interceptors = implClass.getAnnotation(Interceptors.class);
                    if (interceptors != null) {
                        Class<?>[] interceptorsClass = interceptors.value();
                        for (Class<?> c : interceptorsClass) {
                            interceptorsList.add(c.newInstance());
                        }
                    }

                    if (!methods.isEmpty()) {
                        aroundMethod = methods.get(0);
                    }

                    beanInstance = (T) Proxy.newProxyInstance(implClass.getClassLoader(),
                                                              implClass.getInterfaces(),
                                                              new BeanInterceptorInvocationHandler(beanInstance, interceptorsList.toArray(), aroundMethod));

                }


                // Inject annotated fields and methods
                this.injectDependencies(beanInstance, implClass, markMap, newBeansCreated);

                // The bean is created and injected memorize it to call all
                // PostConstruct when all injections are done.
                newBeansCreated.add(beanInstance);

                // Hold the singleton reference
                if (implClass.isAnnotationPresent(Singleton.class)) {
                    this.singletonsBean.put(binding, beanInstance);
                } else {
                    this.prototypesBean.add(beanInstance);
                }

            } catch (InstantiationException e) {
                throw new BeanInstantiationException(e);
            } catch (IllegalAccessException e) {
                throw new BeanInstantiationException(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        return beanInstance;
    }

    /**
     * Inject dependencies in the instance. The SuperClass fields are injected
     * first.
     *
     * @param instance        where inject dependencies annotated by @Inject
     * @param markMap         the map used to resolve cyclic dependencies
     * @param newBeansCreated the list of the new beans created (the bean dependencies)
     * @param clazz           the class who contains the field definition who can be injected
     */
    private void injectDependencies(final Object instance, final Class<?> clazz, final Map<Class<?>, Object> markMap, final List<Object> newBeansCreated) {
        this.logger.debug("Inject fields and methods of class {}", clazz.getSimpleName());

        if (clazz.getSuperclass() != null) { // Inject superclass before
            this.injectDependencies(instance, clazz.getSuperclass(), markMap, newBeansCreated);
        }

        /* ------ Inject Fields ------- */

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            
            if (field.isAnnotationPresent(Inject.class)) {
                // Inject final field is not valid
                if (Modifier.isFinal(field.getModifiers())) {
                    throw new BeanDependencyInjectionException("Final field " + field.getName() + " in class " + field.getDeclaringClass().getName() + " cannot be injected");
                }

                //If this class is not already marked, mark it
                if (!markMap.containsKey(instance.getClass())) {
                    markMap.put(instance.getClass(), instance);
                }

                if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                }

                Object dependency = this.makeInstance(field.getType(), markMap, newBeansCreated);

                try {

                    field.set(ProxyHelper.getTargetObject(instance), dependency);

                } catch (IllegalAccessException ex) {
                    throw new BeanDependencyInjectionException("The field " + field.getName() + "is not accessible and cannot be injected");
                }

            }

        }

        /* ------ Inject Methods ------- */

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {

            if (method.isAnnotationPresent(Inject.class)) {

                if (Modifier.isAbstract(method.getModifiers())) {
                    throw new BeanDependencyInjectionException("Cannot inject dependencies in abstract method " + method.getName() + " of bean " + instance.getClass().getSimpleName());
                }

                // This method is not overidden in subclass
                if (!ReflectionHelper.isOverriden(instance.getClass(), method)) {

                    // If this class is not already marked, mark it
                    if (!markMap.containsKey(instance.getClass())) {
                        markMap.put(instance.getClass(), instance);
                    }

                    if (Modifier.isPrivate(method.getModifiers())) {
                        method.setAccessible(true);
                    }

                    List<Object> paramValue = new ArrayList<Object>();
                    Class<?>[] paramTypes = method.getParameterTypes();
                    for (Class<?> paramType : paramTypes) {
                        if (paramType.equals(clazz)) {
                            throw new BeanDependencyInjectionException("Setter injection cannot have a parameter of type " + clazz.getSimpleName());  
                        }

                        paramValue.add(this.makeInstance(paramType, markMap, newBeansCreated));
                    }

                    // Invoke method
                    try {

                        method.invoke(ProxyHelper.getTargetObject(instance), paramValue.toArray());

                    } catch (IllegalAccessException e) {
                        throw new BeanDependencyInjectionException(e);
                    } catch (InvocationTargetException e) {
                        throw new BeanDependencyInjectionException(e);
                    }

                }

            }

        }

        // Injection is done remove remove it from the MarkMap
        if (markMap.containsKey(instance.getClass())) {
            markMap.remove(instance.getClass());
        }

    }

    /**
     * This method remove all beans references
     * memorised by the singleton for PreDestroy
     * CallBack and Singleton scope.
     *
     * @throws CallbackInvocationException if the invocation of a PreDestroy methods failed
     */
    public void removeAllBeansReferences() {
        //Create list for all beans (prototype and singletons)
        List<Object> allBeans = new ArrayList<Object>() {{
            addAll(prototypesBean);
            addAll(singletonsBean.values());
        }};

        try {

            for (Object bean : allBeans) {
                ReflectionHelper.invokeDeclaredMethodWith(PreDestroy.class, ProxyHelper.getTargetObject(bean));
            }


        } catch (InvocationTargetException e) {
            throw new CallbackInvocationException(e);
        } catch (IllegalAccessException e) {
            throw new CallbackInvocationException(e);
        } finally {
            allBeans.clear();
            this.prototypesBean.clear();
            this.singletonsBean.clear();
        }
    }

}                                                                        

package com.my.container.context.beanfactory;

import com.my.container.binding.Binding;
import com.my.container.binding.BindingHolder;
import com.my.container.binding.MapBindingHolder;
import com.my.container.context.beanfactory.exceptions.BeanDependencyInjectionException;
import com.my.container.context.beanfactory.exceptions.BeanInstantiationException;
import com.my.container.context.beanfactory.exceptions.CallbackInvocationException;
import com.my.container.context.beanfactory.exceptions.NoSuchBeanDefinitionException;
import com.my.container.context.beanfactory.handler.ProxyHelper;
import com.my.container.util.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;


//TODO interceptor verification
//TODO callbacks method verification
//TODO constructor cyclic dependencies
//TODO refactor interceptor
//TODO asynchronous bean destruction can be good if one PreDestroy crash :p
/**
 * The factory of bean class
 *
 * @author kevinpollet
 */
public class BeanFactory {

    private final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private BindingHolder holder;

    private List<Object> prototypesBean;

    private Map<Binding, Object> singletonsBean;

    /**
     * Bean Factory constructor.
     *
     * @param list the binding list
     */
    public BeanFactory(final List<Binding<?>> list) {
        //Populate binding holder
        this.holder = new MapBindingHolder();
        if (list != null) {
            for (Binding b : list) {
                this.holder.put(b);
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

        T createdInstance;
        Binding binding = this.holder.getBindingsFor(clazz);
        if (binding == null) {
            throw new NoSuchBeanDefinitionException("The class " + clazz.getSimpleName() + " have no binding defined");
        }

        List<Object> newBeansCreated = new ArrayList<Object>();
        createdInstance = (T) this.makeInstance(binding, new HashMap<Class<?>, Object>(), newBeansCreated);

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
     * @param markMap         the map used to resolve cyclic dependencies
     * @param newBeansCreated the list of the new beans created (the bean dependencies)
     * @return the newly created instance
     * @throws NoSuchBeanDefinitionException if there is no binding implementation for this bean contract
     */
    private Object makeInstance(final Binding binding, final Map<Class<?>, Object> markMap, final List<Object> newBeansCreated) {
        if (binding.getImplementation() == null) {
            throw new NoSuchBeanDefinitionException("The binding for the class " + binding.getInterface().getSimpleName() + " is not valid");
        }

        Object beanInstance = null;
        Class<?> implClass = binding.getImplementation(); 

        if (implClass.isAnnotationPresent(Singleton.class) && this.singletonsBean.containsKey(binding)) {
            beanInstance = this.singletonsBean.get(binding);

        } else if (markMap.containsKey(implClass)) {
            beanInstance = markMap.get(implClass);
            if (beanInstance == null) {
                throw new BeanDependencyInjectionException("Cyclic dependency injection is not authorized in constructor");
            }

        } else {

            try {


                /*---------- Injection of constructors ----------*/
                /* Inject constructors only first annotated will be used
                   or default constructor if none have been annotated. */

                Constructor<?>[] constructors = implClass.getConstructors();
                for (Constructor<?> constructor : constructors) {

                     if (constructor.isAnnotationPresent(Inject.class)) {

                         List<Object> params = new ArrayList<Object>();
                         Class<?>[] paramsClass = constructor.getParameterTypes();
                         Annotation[][] paramsAnnotations = constructor.getParameterAnnotations();

                         if(paramsClass.length > 0) {
                            markMap.put(implClass, null); // value is null because instance is not created yet
                         }

                         int i = 0;
                         for (Class<?> pClass : paramsClass) {

                             Binding pBinding = null;

                             if (paramsAnnotations[i].length == 0) {
                                 pBinding = this.holder.getBindingsFor(pClass);
                             } else {
                                 for(Annotation annotation : paramsAnnotations[i]) {
                                    if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                                        pBinding = this.holder.getQualifiedBindingFor(pClass, annotation);
                                        break;
                                    }
                                 }
                             }

                             if (pBinding == null) {
                                throw new NoSuchBeanDefinitionException("The class " + pClass.getSimpleName() + " have no binding defined");
                             }

                             params.add(this.makeInstance(pBinding, markMap, newBeansCreated));
                             i++;
                         }

                         beanInstance = constructor.newInstance(params.toArray());
                         break;
                     }

                }

                // No annotated constructor have been found call default
                if (beanInstance == null) {
                    beanInstance = implClass.newInstance();
                }

                // Check SPI for InvocationHandler
                ServiceLoader<BeanInstanceWeaver> loader = ServiceLoader.load(BeanInstanceWeaver.class);
                Iterator<BeanInstanceWeaver> iterator = loader.iterator();
                while (iterator.hasNext()) {
                    BeanInstanceWeaver handler = iterator.next();
                    if (handler.isValidBean(beanInstance)) {
                        beanInstance = handler.weaveBean(beanInstance);
                    }
                }

                // Inject dependencies
                this.injectDependencies(beanInstance, implClass, markMap, newBeansCreated);

                // The bean is created and injected memorize it to call all PostConstruct when all injections are done.
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
                throw new BeanInstantiationException(e);
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
        if (clazz.getSuperclass() != null) { // Inject superclass first
            this.injectDependencies(instance, clazz.getSuperclass(), markMap, newBeansCreated);
        }

        /* ------ Inject Fields ------- */

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(Inject.class)) {

                if (Modifier.isFinal(field.getModifiers())) {
                    throw new BeanDependencyInjectionException("Final field " + field.getName() + " in class " + field.getDeclaringClass().getName() + " cannot be injected");
                }

                // If this class is not already marked, mark it
                if (!markMap.containsKey(instance.getClass())) {
                    markMap.put(instance.getClass(), instance);
                }

                if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                }

                // Check if injection is qualified
                Binding fieldBinding = null;
                Annotation[] annotations = field.getAnnotations();

                if (annotations.length == 1) {
                    fieldBinding = this.holder.getBindingsFor(field.getType());
                } else {
                    for (Annotation annotation : annotations) {
                        if(annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                           fieldBinding = this.holder.getQualifiedBindingFor(field.getType(), annotation);
                           break;
                        }
                    }
                }

                if (fieldBinding == null) {
                   throw new NoSuchBeanDefinitionException("The class " + field.getType().getSimpleName() + " have no binding defined");
                }

                Object dependency = this.makeInstance(fieldBinding, markMap, newBeansCreated);

                try {

                    field.set(ProxyHelper.getTargetObject(instance), dependency);

                } catch (IllegalAccessException e) {
                    throw new BeanDependencyInjectionException("The field " + field.getName() + "is not accessible and cannot be injected", e);
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
                if (!ReflectionHelper.isOverridden(instance.getClass(), method)) {

                    // If this class is not already marked, mark it
                    if (!markMap.containsKey(instance.getClass())) {
                        markMap.put(instance.getClass(), instance);
                    }

                    if (Modifier.isPrivate(method.getModifiers())) {
                        method.setAccessible(true);
                    }

                    List<Object> params = new ArrayList<Object>();
                    Class<?>[] paramsClass = method.getParameterTypes();
                    Annotation[][] paramsAnnotation = method.getParameterAnnotations();

                    int i = 0;
                    for (Class<?> pClass : paramsClass) {

                        if (pClass.equals(clazz)) {
                            throw new BeanDependencyInjectionException("Setter injection cannot have a parameter of type " + pClass.getSimpleName());
                        }

                        Binding pBinding = null;

                        if (paramsAnnotation[i].length == 0) {
                            pBinding = this.holder.getBindingsFor(pClass);
                        } else {
                            for (Annotation annotation : paramsAnnotation[i]) {
                                if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                                    pBinding = this.holder.getQualifiedBindingFor(pClass, annotation); 
                                    break;
                                }
                            }
                        }

                        if (pBinding == null) {
                           throw new NoSuchBeanDefinitionException("The class " + pClass.getSimpleName() + " have no binding defined");
                        }

                        params.add(this.makeInstance(pBinding, markMap, newBeansCreated));
                        i++;
                    }

                    try {

                        method.invoke(ProxyHelper.getTargetObject(instance), params.toArray());

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
     * <p>
     * This method permits to resolve dependencies of an
     * existing bean. Generally this bean was created with
     * the Java new operator.
     * </p>
     *
     * @param bean the existing bean
     */
    public void resolveDependencies(final Object bean) {

        List<Object> newlyCreatedBean = new ArrayList<Object>();
        this.injectDependencies(bean, bean.getClass(), new HashMap<Class<?>, Object>(), newlyCreatedBean);

        // All objects are injected call the PostContruct methods in the right order
        try {

            for (Object instance : newlyCreatedBean) {
                ReflectionHelper.invokeDeclaredMethodWith(PostConstruct.class, ProxyHelper.getTargetObject(instance));
            }

        } catch (InvocationTargetException e) {
            throw new CallbackInvocationException(e);
        } catch (IllegalAccessException e) {
            throw new CallbackInvocationException(e);
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

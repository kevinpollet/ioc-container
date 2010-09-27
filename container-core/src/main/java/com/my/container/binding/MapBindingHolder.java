package com.my.container.binding;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The bindings map implementation
 * with a HashMap.
 *
 * @author kevinpollet
 */
public class MapBindingHolder implements BindingHolder {

    /**
     * The bindings map.
     */
    private Map<Class<?>, List<Binding<?>>> bindings;

    /**
     * The default constructor. 
     */
    public MapBindingHolder() {
        this.bindings = new HashMap<Class<?>, List<Binding<?>>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final Binding<?> binding) {
        if (binding == null) {
            throw new IllegalArgumentException("The binding parameter cannot be null");
        }

        List<Binding<?>> bindingList = this.bindings.get(binding.getInterface());
        if (bindingList == null) {
            bindingList = new ArrayList<Binding<?>>();
        }

        bindingList.add(binding);
        this.bindings.put(binding.getInterface(), bindingList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBindingFor(final Class<?> clazz) {
        return this.bindings.containsKey(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Binding<?> getBindingsFor(final Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        }

        Binding<?> result = null;

        List<Binding<?>> bindingList = this.bindings.get(clazz);
        if (bindingList != null) {
            for (Binding b : bindingList) {
                if (b.getName() == null && b.getQualifier() == null) {
                    result = b;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Q extends Annotation> Binding<?> getQualifiedBindingFor(final Class<?> clazz, final Q qualifier) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        } else if (qualifier == null || !qualifier.annotationType().isAnnotationPresent(Qualifier.class)) {
            throw new IllegalArgumentException("The qualifier cannot be null or must be annotated with @Qualifier");
        }

        Binding<?> result = null;

        List<Binding<?>> bindingList = this.bindings.get(clazz);
        if (bindingList != null) {
            for (Binding b : bindingList) {
                if ((qualifier instanceof Named && ((Named) qualifier).value().equals(b.getName())) ||
                    qualifier.annotationType().equals(b.getQualifier())) {
                    
                    result = b;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Binding<?>> removeAllBindingFor(final Class<?> clazz) {
       return this.bindings.remove(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Q extends Annotation> Binding<?> removeQualifiedBindingFor(final Class<?> clazz, final Q qualifier) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        } else if (qualifier == null || !qualifier.annotationType().isAnnotationPresent(Qualifier.class)) {
            throw new IllegalArgumentException("The qualifier cannot be null or must be annotated with @Qualifier");
        }

        Binding<?> result = null;

        List<Binding<?>> bindingList = this.bindings.get(clazz);
        if (bindingList != null) {
            for (Binding b : bindingList) {
                if ((qualifier instanceof Named && ((Named) qualifier).value().equals(b.getName())) ||
                    qualifier.annotationType().equals(b.getQualifier())) {

                    result = b;
                    break;
                }
            }
        }

        bindingList.remove(result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllBindings() {
        this.bindings.clear();
    }


}

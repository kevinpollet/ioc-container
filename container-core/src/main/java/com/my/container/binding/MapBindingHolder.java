/*
 * Copyright 2010 Kevin Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    public void put(final Binding<?> binding) {
        if (binding == null) {
            throw new IllegalArgumentException("The binding parameter cannot be null");
        }

        List<Binding<?>> bindingList = this.bindings.get(binding.getClazz());
        if (bindingList == null) {
            bindingList = new ArrayList<Binding<?>>();
        }

        bindingList.add(binding);
        this.bindings.put(binding.getClazz(), bindingList);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBindingFor(final Class<?> clazz) {
        return this.bindings.containsKey(clazz);
    }

    /**
     * {@inheritDoc}
     */
    public <T> Binding<T> getBindingFor(final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The clazz parameter cannot be null");
        }

        Binding<T> result = null;

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
    public List<Binding<?>> removeAllBindingFor(final Class<?> clazz) {
       return this.bindings.remove(clazz);
    }

    /**
     * {@inheritDoc}
     */
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
    public void removeAllBindings() {
        this.bindings.clear();
    }


}

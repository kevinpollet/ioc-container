package com.my.container.binding;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * This interface is the contract for
 * a binding holder.
 *
 * @author kevinpollet
 */
public interface BindingHolder {

    /**
     * Put a bindings in this binding collection.
     *
     * @param binding the binding
     */
    public void put(final Binding<?> binding);

    /**
     * There is a binding for the given class.
     *
     * @param clazz the class
     * @return true if one, false otherwise
     */
    public boolean isBindingFor(final Class<?> clazz);

    /**
     * Retrieve all binding defined ro the given class.
     *
     * @param clazz the class
     * @param <T> the type of the binding
     * @return null if none or the binding list
     */
    public <T>  Binding<T> getBindingFor(final Class<T> clazz);

    /**
     * Get the binding defined for the given class with
     * the given qualifier.
     *
     * @param clazz the class
     * @param qualifier the binding qualifier
     * @param <Q> the qualifier type
     * @return null if none or the binding
     */
    public <Q extends Annotation> Binding<?> getQualifiedBindingFor(final Class<?> clazz, final Q qualifier);

    /**
     * Remove all the bindings for the given class.
     *
     * @param clazz the class
     * @return null if none or the list removed
     */
    public List<Binding<?>> removeAllBindingFor(final Class<?> clazz);

    /**
     * Remove the qualified binding for
     * the given class.
     *
     * @param clazz the class
     * @param qualifier the qualifier
     * @param <Q> the qualifier type
     * @return null if none or the biding removed
     */
    public <Q extends Annotation> Binding<?> removeQualifiedBindingFor(final Class<?> clazz, final Q qualifier);

    /**
     * Remove all bindings contains
     * in this Collection.
     */
    public void removeAllBindings();

}

package org.joeyb.undercarriage.core.utils;

import net.jodah.typetools.TypeResolver;

/**
 * {@code GenericClass} exists in order to work around Java's type erasure. If you call {@link #getGenericClass()} on
 * a child instance (either from a concrete or anonymous child class), then you can get a {@link Class} instance for the
 * generic type {@code T}.
 *
 * @param <T> the type whose {@link Class} we are requesting
 */
public abstract class GenericClass<T> {

    /**
     * Returns the {@link Class} for the generic type {@code T}.
     */
    @SuppressWarnings("unchecked")
    public Class<T> getGenericClass() {
        return (Class<T>) TypeResolver.resolveRawArgument(GenericClass.class, getClass());
    }
}

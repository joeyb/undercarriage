package org.joeyb.undercarriage.core.utils;

import net.jodah.typetools.TypeResolver;

public abstract class GenericClass<T> {

    @SuppressWarnings("unchecked")
    public Class<T> getGenericClass() {
        return (Class<T>) TypeResolver.resolveRawArgument(GenericClass.class, getClass());
    }
}

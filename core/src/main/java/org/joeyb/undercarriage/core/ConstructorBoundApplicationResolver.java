package org.joeyb.undercarriage.core;

import org.joeyb.undercarriage.core.utils.Suppliers;

import java.util.function.Supplier;

/**
 * {@code ConstructorBoundApplicationResolver} is an implementation of {@link ApplicationResolver}. It memoizes the
 * value returned by the given {@link Application} {@link Supplier}.
 */
public class ConstructorBoundApplicationResolver extends ApplicationResolverBase {

    private final Supplier<Application<?>> applicationSupplier;

    public ConstructorBoundApplicationResolver(Supplier<Application<?>> applicationSupplier) {
        this.applicationSupplier = Suppliers.memoize(applicationSupplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Application<?> application() {
        return checkApplication(applicationSupplier.get());
    }
}

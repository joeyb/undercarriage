package org.joeyb.undercarriage.core;

import org.joeyb.undercarriage.core.utils.Suppliers;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@code DefaultApplicationResolver} is the default implementation of {@link ApplicationResolver}. It memoizes the
 * value returned by the given {@link Application} {@link Supplier}.
 */
public class DefaultApplicationResolver implements ApplicationResolver {

    private final Supplier<Application<?>> applicationSupplier;

    public DefaultApplicationResolver(Supplier<Application<?>> applicationSupplier) {
        this.applicationSupplier = Suppliers.memoize(applicationSupplier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Application<?> application() {
        return checkApplication(applicationSupplier.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <AppT extends Application<?>> AppT application(Class<AppT> applicationClass) {
        return optionalApplication(applicationClass).orElseThrow(() -> new IllegalArgumentException(
                String.format("Actual application is not of type %s.", applicationClass.getSimpleName())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <AppT extends Application<?>> Optional<AppT> optionalApplication(
            Class<AppT> applicationClass) {

        final Application<?> application = application();

        Class<? extends Application> actualApplicationClass = application.getClass();

        return applicationClass.isAssignableFrom(actualApplicationClass)
               ? Optional.of((AppT) application)
               : Optional.empty();
    }

    private Application<?> checkApplication(Application<?> application) {
        if (application == null) {
            throw new IllegalStateException("Application has not been provided to the ApplicationResolver.");
        }

        return application;
    }
}

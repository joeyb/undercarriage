package org.joeyb.undercarriage.core;

import java.util.Optional;

/**
 * {@code ApplicationResolverBase} provides an abstract base implementation of {@link ApplicationResolver}.
 */
public abstract class ApplicationResolverBase implements ApplicationResolver {

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

        final Application<?> application = checkApplication(application());

        Class<? extends Application> actualApplicationClass = application.getClass();

        return applicationClass.isAssignableFrom(actualApplicationClass)
               ? Optional.of((AppT) application)
               : Optional.empty();
    }

    /**
     * Returns the given {@link Application} if it's not null, otherwise throws.
     *
     * @param application the application instance
     * @throws IllegalStateException if the application is null
     */
    protected final Application<?> checkApplication(Application<?> application) {
        if (application == null) {
            throw new IllegalStateException("Application has not been provided to the ApplicationResolver.");
        }

        return application;
    }
}

package org.joeyb.undercarriage.core;

import static java.util.Objects.requireNonNull;

/**
 * {@code LateBoundApplicationResolver} is an implementation of {@link ApplicationResolver}. It allows the application
 * instance to be set after construction to help avoid circular dependency issues.
 */
public class LateBoundApplicationResolver extends ApplicationResolverBase {

    private final Object lock = new Object();

    private volatile Application<?> application;

    /**
     * {@inheritDoc}
     */
    @Override
    public Application<?> application() {
        return checkApplication(application);
    }

    /**
     * Sets the application instance that will be returned by {@link #application()}. The application can only be set
     * once, any subsequent calls to this method will throw.
     *
     * @param application the application instance
     * @throws IllegalStateException if the method is called more than once
     */
    public void setApplication(Application<?> application) {
        if (this.application == null) {
            synchronized (lock) {
                if (this.application == null) {
                    this.application = requireNonNull(application);
                    return;
                }
            }
        }

        throw new IllegalStateException("Application can only be set once.");
    }
}

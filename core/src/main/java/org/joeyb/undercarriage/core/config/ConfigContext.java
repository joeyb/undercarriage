package org.joeyb.undercarriage.core.config;

/**
 * {@code ConfigContext} is the base interface for accessing the application's config.
 *
 * @param <ConfigT> the app's config type
 */
public interface ConfigContext<ConfigT extends ConfigSection> {

    /**
     * Returns the application's config.
     */
    ConfigT config();
}

package org.joeyb.undercarriage.jersey;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.Application;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;

/**
 * {@code JerseyApplication} is the base interface for all Jersey-based applications. It defines the Jersey-specific
 * application lifecycle phases and provides access to the underlying Jersey {@link ResourceConfig} instance. Since
 * Jersey is strongly tied to HK2, all {@link JerseyApplication}-based apps are expected to use HK2 and the interface
 * exposes the app's {@link ServiceLocator}.
 *
 * @param <ConfigT> the app's config type
 */
public interface JerseyApplication<ConfigT extends JerseyConfigSection> extends Application<ConfigT> {

    /**
     * Returns the app's actual port. The port is only known after the app has been started since a configured value of
     * 0 will cause the server to bind to a random available port.
     *
     * @throws IllegalStateException if the app has not been started
     */
    int port();

    /**
     * Returns the application's underlying {@link ResourceConfig}.
     */
    ResourceConfig resourceConfig();

    /**
     * Returns the application's {@link ServiceLocator}. Jersey is strongly tied to HK2, so all Jersey-based apps are
     * expected to use HK2.
     */
    ServiceLocator serviceLocator();
}

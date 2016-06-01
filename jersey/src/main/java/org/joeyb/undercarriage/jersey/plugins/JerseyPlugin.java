package org.joeyb.undercarriage.jersey.plugins;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.jersey.JerseyApplication;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;

/**
 * {@code JerseyPlugin} is the base interface for all {@link JerseyApplication} plugins.
 *
 * @param <ConfigT> the app's config type
 */
public interface JerseyPlugin<ConfigT extends JerseyConfigSection> extends Plugin<ConfigT> {

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

package org.joeyb.undercarriage.jersey.plugins;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.PluginBase;
import org.joeyb.undercarriage.jersey.JerseyApplication;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;

/**
 * {@code JerseyPluginBase} provides a base default implementation for the {@link JerseyPlugin} interface.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class JerseyPluginBase<ConfigT extends JerseyConfigSection>
        extends PluginBase<ConfigT>
        implements JerseyPlugin<ConfigT> {

    protected JerseyPluginBase(
            ApplicationResolver applicationResolver,
            ConfigContext<? extends ConfigT> configContext) {
        super(applicationResolver, configContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceConfig resourceConfig() {
        return applicationResolver().application(JerseyApplication.class).resourceConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ServiceLocator serviceLocator() {
        return applicationResolver().application(JerseyApplication.class).serviceLocator();
    }
}

package org.joeyb.undercarriage.jersey.hk2;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.jersey.JerseyApplicationBase;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;

import javax.inject.Singleton;

/**
 * {@code JerseyApplicationBaseBinder} provides default bindings for dependencies required by
 * {@link JerseyApplicationBase}. Most Jersey applications should install this binder at the beginning of their app's
 * {@link AbstractBinder#configure()} method.
 */
public class JerseyApplicationBaseBinder<ConfigT extends JerseyConfigSection> extends AbstractBinder {

    private final Class<? extends Factory<ConfigContext<ConfigT>>> configContextFactory;

    public JerseyApplicationBaseBinder(Class<? extends Factory<ConfigContext<ConfigT>>> configContextFactory) {
        this.configContextFactory = configContextFactory;
    }

    /**
     * Configures default binding for dependencies required by {@link JerseyApplicationBase}.
     */
    @Override
    protected void configure() {
        bind(new LateBoundApplicationResolver())
                .to(ApplicationResolver.class)
                .to(LateBoundApplicationResolver.class);

        bind(new ResourceConfig())
                .to(ResourceConfig.class);

        bindFactory(configContextFactory)
                .to(new TypeLiteral<ConfigContext<ConfigT>>() { })
                .in(Singleton.class);
    }
}

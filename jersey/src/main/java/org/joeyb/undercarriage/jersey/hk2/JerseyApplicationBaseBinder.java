package org.joeyb.undercarriage.jersey.hk2;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.joeyb.undercarriage.jersey.JerseyApplicationBase;

/**
 * {@code JerseyApplicationBaseBinder} provides default bindings for dependencies required by
 * {@link JerseyApplicationBase}. Most Jersey applications should install this binder at the beginning of their app's
 * {@link AbstractBinder#configure()} method.
 */
public class JerseyApplicationBaseBinder extends AbstractBinder {

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
    }
}

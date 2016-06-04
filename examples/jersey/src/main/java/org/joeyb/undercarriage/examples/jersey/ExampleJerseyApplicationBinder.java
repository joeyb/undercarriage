package org.joeyb.undercarriage.examples.jersey;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.jersey.hk2.JerseyApplicationBaseBinder;

public class ExampleJerseyApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        install(new JerseyApplicationBaseBinder<>(configContextFactory()));
    }

    protected Class<? extends Factory<ConfigContext<ExampleJerseyConfig>>> configContextFactory() {
        return DefaultConfigContextFactory.class;
    }
}

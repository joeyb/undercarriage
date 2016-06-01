package org.joeyb.undercarriage.jersey.example;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;

public class ExampleJerseyApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        final ExampleJerseyApplicationConfig config = ImmutableExampleJerseyApplicationConfig.builder()
                .jersey(ImmutableJerseyConfig.builder()
                                .baseUri("http://localhost:0")
                                .joinServerThread(false)
                                .build())
                .build();

        bind(new LateBoundApplicationResolver())
                .to(ApplicationResolver.class)
                .to(LateBoundApplicationResolver.class);

        bind(new ManualConfigContext<>(config))
                .to(new TypeLiteral<ConfigContext<ExampleJerseyApplicationConfig>>() { })
                .to(new TypeLiteral<ConfigContext<? extends JerseyConfigSection>>() { });

        bind(new ResourceConfig())
                .to(ResourceConfig.class);
    }
}

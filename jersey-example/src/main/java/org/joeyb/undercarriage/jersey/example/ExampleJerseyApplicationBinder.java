package org.joeyb.undercarriage.jersey.example;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.joeyb.undercarriage.jersey.hk2.JerseyApplicationBaseBinder;

public class ExampleJerseyApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        install(new JerseyApplicationBaseBinder());

        final ExampleJerseyApplicationConfig config = ImmutableExampleJerseyApplicationConfig.builder()
                .jersey(ImmutableJerseyConfig.builder()
                                .baseUri("http://localhost:0")
                                .joinServerThread(false)
                                .build())
                .build();

        bind(new ManualConfigContext<>(config))
                .to(new TypeLiteral<ConfigContext<ExampleJerseyApplicationConfig>>() { })
                .to(new TypeLiteral<ConfigContext<? extends JerseyConfigSection>>() { });
    }
}

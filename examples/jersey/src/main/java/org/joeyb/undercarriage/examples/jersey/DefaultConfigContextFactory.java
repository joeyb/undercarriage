package org.joeyb.undercarriage.examples.jersey;

import org.glassfish.hk2.api.Factory;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;

public class DefaultConfigContextFactory implements Factory<ConfigContext<ExampleJerseyConfig>> {

    @Override
    public void dispose(ConfigContext<ExampleJerseyConfig> instance) {

    }

    @Override
    public ConfigContext<ExampleJerseyConfig> provide() {
        return new ManualConfigContext<>(
                ImmutableExampleJerseyConfig.builder()
                        .jersey(ImmutableJerseyConfig.builder()
                                        .baseUri("http://localhost:0")
                                        .joinServerThread(true)
                                        .build())
                        .build());
    }
}

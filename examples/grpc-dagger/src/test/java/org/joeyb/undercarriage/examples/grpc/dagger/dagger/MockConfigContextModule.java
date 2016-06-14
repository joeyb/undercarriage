package org.joeyb.undercarriage.examples.grpc.dagger.dagger;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.testing.config.MockConfigContext;
import org.joeyb.undercarriage.examples.grpc.dagger.HelloWorldConfig;

public class MockConfigContextModule extends ConfigContextModule {

    @Override
    ConfigContext<HelloWorldConfig> provideConfigContext() {
        return new MockConfigContext<HelloWorldConfig>(super.provideConfigContext()) { };
    }
}

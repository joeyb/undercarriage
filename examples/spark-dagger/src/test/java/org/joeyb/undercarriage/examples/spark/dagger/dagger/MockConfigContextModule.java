package org.joeyb.undercarriage.examples.spark.dagger.dagger;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.testing.config.MockConfigContext;
import org.joeyb.undercarriage.examples.spark.dagger.SparkDaggerConfig;

public class MockConfigContextModule extends ConfigContextModule {

    @Override
    ConfigContext<SparkDaggerConfig> provideConfigContext() {
        return new MockConfigContext<SparkDaggerConfig>(super.provideConfigContext()) { };
    }
}

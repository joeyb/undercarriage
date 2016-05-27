package org.joeyb.undercarriage.example.dagger;

import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.example.ExampleConfig;
import org.joeyb.undercarriage.example.ExampleConfigSection;
import org.joeyb.undercarriage.example.ImmutableExampleConfig;

import java.util.UUID;

import javax.inject.Singleton;

/**
 * {@code ConfigModule} defines all providers for the application's {@link ConfigContext}.
 */
@Module
public class ConfigModule {

    @Provides
    @Singleton
    static ConfigContext<ExampleConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableExampleConfig.builder()
                        .someOtherSetting(UUID.randomUUID().toString())
                        .someSetting(UUID.randomUUID().toString())
                        .build());
    }

    @Provides
    static ConfigContext<? extends ExampleConfigSection> provideConfigContextExampleConfigSection(
            ConfigContext<ExampleConfig> configContext) {
        return configContext;
    }
}

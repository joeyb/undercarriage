package org.joeyb.undercarriage.example.dagger;

import dagger.Module;
import dagger.Provides;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.example.ExampleConfig;
import org.joeyb.undercarriage.example.ExampleConfigSection;
import org.joeyb.undercarriage.example.ImmutableExampleConfig;

import javax.inject.Singleton;
import java.util.UUID;

@Module
public class ConfigModule {

    @Provides
    @Singleton
    public static ConfigContext<ExampleConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableExampleConfig.builder()
                        .someOtherSetting(UUID.randomUUID().toString())
                        .someSetting(UUID.randomUUID().toString())
                        .build());
    }

    @Provides
    public static ConfigContext<? extends ExampleConfigSection> provideConfigContextExampleConfigSection(
            ConfigContext<ExampleConfig> configContext) {
        return configContext;
    }
}

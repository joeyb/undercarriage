package org.joeyb.undercarriage.example.dagger;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.ConstructorBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.example.ExampleApplication;
import org.joeyb.undercarriage.example.ExampleConfig;
import org.joeyb.undercarriage.example.ExampleConfigSection;
import org.joeyb.undercarriage.example.ImmutableExampleConfig;

import java.util.UUID;

import javax.inject.Singleton;

@Module
public class ExampleApplicationModule {

    @Provides
    @Singleton
    ApplicationResolver provideApplicationResolver(Lazy<ExampleApplication> application) {
        return new ConstructorBoundApplicationResolver(application::get);
    }

    @Provides
    @Singleton
    ConfigContext<ExampleConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableExampleConfig.builder()
                        .someOtherSetting(UUID.randomUUID().toString())
                        .someSetting(UUID.randomUUID().toString())
                        .build());
    }

    @Provides
    ConfigContext<? extends ExampleConfigSection> provideConfigContextExampleConfigSection(
            ConfigContext<ExampleConfig> configContext) {
        return configContext;
    }
}

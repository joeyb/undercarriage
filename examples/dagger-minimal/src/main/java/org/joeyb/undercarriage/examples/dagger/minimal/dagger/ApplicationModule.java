package org.joeyb.undercarriage.examples.dagger.minimal.dagger;

import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.examples.dagger.minimal.DaggerMinimalConfig;
import org.joeyb.undercarriage.examples.dagger.minimal.ImmutableDaggerMinimalConfig;

@Module
public class ApplicationModule {

    @Provides
    public ConfigContext<DaggerMinimalConfig> provideConfigContext() {
        return new ManualConfigContext<>(ImmutableDaggerMinimalConfig.builder().build());
    }
}

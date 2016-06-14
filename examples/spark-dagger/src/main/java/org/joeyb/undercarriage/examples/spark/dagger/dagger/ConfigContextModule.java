package org.joeyb.undercarriage.examples.spark.dagger.dagger;

import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.examples.spark.dagger.ImmutableSparkDaggerConfig;
import org.joeyb.undercarriage.examples.spark.dagger.SparkDaggerConfig;
import org.joeyb.undercarriage.spark.config.ImmutableSparkConfig;

import javax.inject.Singleton;

@Module
public class ConfigContextModule {

    @Provides
    @Singleton
    ConfigContext<SparkDaggerConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableSparkDaggerConfig.builder()
                        .spark(
                                ImmutableSparkConfig.builder()
                                        .port(0)
                                        .build())
                        .build());
    }
}

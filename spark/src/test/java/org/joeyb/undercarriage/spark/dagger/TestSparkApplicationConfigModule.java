package org.joeyb.undercarriage.spark.dagger;

import dagger.Module;
import dagger.Provides;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.spark.ImmutableTestSparkConfig;
import org.joeyb.undercarriage.spark.TestSparkConfig;
import org.joeyb.undercarriage.spark.config.ImmutableSparkConfig;

import javax.inject.Singleton;

@Module
public class TestSparkApplicationConfigModule {

    @Provides
    @Singleton
    public static ConfigContext<TestSparkConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableTestSparkConfig.builder()
                        .spark(ImmutableSparkConfig.builder()
                                .port(0)
                                .build())
                        .build());
    }
}

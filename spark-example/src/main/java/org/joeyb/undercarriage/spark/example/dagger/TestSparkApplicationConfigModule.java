package org.joeyb.undercarriage.spark.example.dagger;

import dagger.Module;
import dagger.Provides;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.spark.config.ImmutableSparkConfig;
import org.joeyb.undercarriage.spark.example.ImmutableTestSparkConfig;
import org.joeyb.undercarriage.spark.example.TestSparkConfig;

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

    @Provides
    public static ConfigContext<? extends TestSparkConfig> provideTestSparkConfigConfigContext(
            ConfigContext<TestSparkConfig> configContext) {

        return configContext;
    }
}

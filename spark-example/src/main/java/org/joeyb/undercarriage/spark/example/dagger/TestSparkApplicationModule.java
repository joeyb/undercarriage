package org.joeyb.undercarriage.spark.example.dagger;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.ConstructorBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.spark.config.ImmutableSparkConfig;
import org.joeyb.undercarriage.spark.example.ImmutableTestSparkConfig;
import org.joeyb.undercarriage.spark.example.TestSparkApplication;
import org.joeyb.undercarriage.spark.example.TestSparkConfig;

import javax.inject.Singleton;

@Module
public class TestSparkApplicationModule {

    @Provides
    @Singleton
    ApplicationResolver provideApplicationResolver(Lazy<TestSparkApplication> application) {
        return new ConstructorBoundApplicationResolver(application::get);
    }

    @Provides
    @Singleton
    ConfigContext<TestSparkConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableTestSparkConfig.builder()
                        .spark(ImmutableSparkConfig.builder()
                                .port(0)
                                .build())
                        .build());
    }

    @Provides
    ConfigContext<? extends TestSparkConfig> provideTestSparkConfigConfigContext(
            ConfigContext<TestSparkConfig> configContext) {

        return configContext;
    }
}

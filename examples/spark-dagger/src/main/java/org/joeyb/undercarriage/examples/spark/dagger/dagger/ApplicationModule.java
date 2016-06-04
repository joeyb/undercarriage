package org.joeyb.undercarriage.examples.spark.dagger.dagger;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.ConstructorBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.examples.spark.dagger.ImmutableSparkDaggerConfig;
import org.joeyb.undercarriage.examples.spark.dagger.SparkDaggerApplication;
import org.joeyb.undercarriage.examples.spark.dagger.SparkDaggerConfig;
import org.joeyb.undercarriage.spark.config.ImmutableSparkConfig;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;

import spark.Service;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    ApplicationResolver provideApplicationResolver(Lazy<SparkDaggerApplication> application) {
        return new ConstructorBoundApplicationResolver(application::get);
    }

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

    @Provides
    ConfigContext<? extends SparkConfigSection> provideSparkConfigSectionConfigContext(
            ConfigContext<SparkDaggerConfig> configContext) {
        return configContext;
    }

    @Provides
    @Singleton
    Service provideSparkService() {
        return Service.ignite();
    }
}

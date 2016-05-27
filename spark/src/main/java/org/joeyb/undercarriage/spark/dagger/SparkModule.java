package org.joeyb.undercarriage.spark.dagger;

import dagger.Module;
import dagger.Provides;

import spark.Service;

import javax.inject.Singleton;

/**
 * {@code SparkModule} contains default dependency satisfiers for Spark applications.
 */
@Module
public class SparkModule {

    /**
     * Returns the {@link Service} instance to be used by the application.
     */
    @Provides
    @Singleton
    public Service provideSparkService() {
        return Service.ignite();
    }
}

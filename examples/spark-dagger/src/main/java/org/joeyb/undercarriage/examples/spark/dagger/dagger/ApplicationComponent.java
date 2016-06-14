package org.joeyb.undercarriage.examples.spark.dagger.dagger;

import dagger.Component;

import org.joeyb.undercarriage.examples.spark.dagger.SparkDaggerApplication;

import javax.inject.Singleton;

@Component(modules = {ApplicationModule.class, ConfigContextModule.class})
@Singleton
public interface ApplicationComponent {

    SparkDaggerApplication application();
}

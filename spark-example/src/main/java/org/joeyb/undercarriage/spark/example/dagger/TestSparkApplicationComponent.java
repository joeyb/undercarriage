package org.joeyb.undercarriage.spark.example.dagger;

import dagger.Component;

import org.joeyb.undercarriage.spark.example.TestSparkApplication;

import javax.inject.Singleton;

@Component(modules = {TestSparkApplicationConfigModule.class, SparkModule.class})
@Singleton
public interface TestSparkApplicationComponent {

    TestSparkApplication application();
}

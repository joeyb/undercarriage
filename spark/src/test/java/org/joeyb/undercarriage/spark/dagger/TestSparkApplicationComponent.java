package org.joeyb.undercarriage.spark.dagger;

import dagger.Component;
import org.joeyb.undercarriage.spark.TestSparkApplication;

import javax.inject.Singleton;

@Component(modules = {TestSparkApplicationConfigModule.class, SparkModule.class})
@Singleton
public interface TestSparkApplicationComponent {

    TestSparkApplication application();
}

package org.joeyb.undercarriage.spark;

import org.joeyb.undercarriage.core.config.ConfigContext;
import spark.Service;

import javax.inject.Inject;

public class TestSparkApplication extends SparkApplicationBase<TestSparkConfig> {

    @Inject
    protected TestSparkApplication(ConfigContext<TestSparkConfig> configContext, Service service) {
        super(configContext, service);
    }

    @Override
    public void start() {
        super.start();

        service().get("/ping", (req, res) -> "pong");
    }
}

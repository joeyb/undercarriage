package org.joeyb.undercarriage.spark;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.spark.plugins.SparkPluginBase;
import spark.Service;

import javax.inject.Inject;

public class TestSparkPlugin extends SparkPluginBase<TestSparkConfig> {

    @Inject
    public TestSparkPlugin(ConfigContext<? extends TestSparkConfig> configContext, Service service) {
        super(configContext, service);
    }

    @Override
    public void start() {
        service().get("/plugin-ping", (req, res) -> "plugin-pong");
    }
}

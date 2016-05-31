package org.joeyb.undercarriage.spark.example;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.spark.plugins.SparkPluginBase;

import spark.Service;

import javax.inject.Inject;

public class TestSparkPlugin extends SparkPluginBase<TestSparkConfig> {

    @Inject
    public TestSparkPlugin(
            ApplicationResolver applicationResolver,
            ConfigContext<? extends TestSparkConfig> configContext,
            Service service) {
        super(applicationResolver, configContext, service);
    }

    @Override
    protected void onStart() {
        service().get("/plugin-ping", (req, res) -> "plugin-pong");
    }
}

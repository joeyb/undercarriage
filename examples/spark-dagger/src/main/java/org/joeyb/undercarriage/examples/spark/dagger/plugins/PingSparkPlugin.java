package org.joeyb.undercarriage.examples.spark.dagger.plugins;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import org.joeyb.undercarriage.spark.plugins.SparkPluginBase;

import spark.Service;

import javax.inject.Inject;

public class PingSparkPlugin extends SparkPluginBase<SparkConfigSection> {

    @Inject
    public PingSparkPlugin(
            ApplicationResolver applicationResolver,
            ConfigContext<? extends SparkConfigSection> configContext,
            Service service) {
        super(applicationResolver, configContext, service);
    }

    @Override
    protected void onStart() {
        service().get("/plugin-ping", (req, res) -> "plugin-pong");
    }
}

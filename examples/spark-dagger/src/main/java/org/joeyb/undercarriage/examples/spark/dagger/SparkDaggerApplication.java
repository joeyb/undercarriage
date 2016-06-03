package org.joeyb.undercarriage.examples.spark.dagger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.examples.spark.dagger.plugins.PingSparkPlugin;
import org.joeyb.undercarriage.spark.SparkApplicationBase;

import spark.Service;

import javax.inject.Inject;

public class SparkDaggerApplication extends SparkApplicationBase<SparkDaggerConfig> {

    private final Iterable<Plugin<? super SparkDaggerConfig>> plugins;

    @Inject
    public SparkDaggerApplication(
            ConfigContext<SparkDaggerConfig> configContext,
            PingSparkPlugin pingSparkPlugin,
            Service service) {
        super(configContext, service);

        plugins = ImmutableList.of(pingSparkPlugin);
    }

    @Override
    protected Iterable<Plugin<? super SparkDaggerConfig>> enabledPlugins() {
        return Iterables.concat(super.enabledPlugins(), plugins);
    }

    @Override
    protected void onStart() {
        super.onStart();

        service().get("/ping", (req, res) -> "pong");
    }
}

package org.joeyb.undercarriage.spark;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.Plugin;
import spark.Service;

import javax.inject.Inject;

public class TestSparkApplication extends SparkApplicationBase<TestSparkConfig> {

    private final Iterable<Plugin<? super TestSparkConfig>> plugins;

    @Inject
    protected TestSparkApplication(
            ConfigContext<TestSparkConfig> configContext,
            Service service,
            TestSparkPlugin testSparkPlugin) {

        super(configContext, service);

        this.plugins = ImmutableList.of(testSparkPlugin);
    }

    @Override
    protected Iterable<Plugin<? super TestSparkConfig>> enabledPlugins() {
        return Iterables.concat(super.enabledPlugins(), plugins);
    }

    @Override
    public void start() {
        super.start();

        service().get("/ping", (req, res) -> "pong");
    }
}

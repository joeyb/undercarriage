package org.joeyb.undercarriage.spark.plugins;

import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.spark.SparkApplication;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;

import spark.Service;

/**
 * {@code SparkPlugin} is the base interface for all {@link SparkApplication} plugins.
 *
 * @param <ConfigT> the app's config type
 */
public interface SparkPlugin<ConfigT extends SparkConfigSection> extends Plugin<ConfigT> {

    /**
     * Returns the app's underlying Spark {@link Service} instance. It's important for consumers to be aware that Spark
     * is set up to automatically start the underlying web server as soon as a route is added, so the route
     * configuration should all be done during the plugin's {@link Plugin#start()} lifecycle phase.
     */
    Service service();
}

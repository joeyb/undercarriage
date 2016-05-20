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
     * Returns the app's underlying Spark {@link Service} instance.
     */
    Service service();
}

package org.joeyb.undercarriage.spark;

import org.joeyb.undercarriage.core.Application;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import spark.Service;

/**
 * {@code SparkApplication} is the base interface for all Spark-based applications. It defines the Spark-specific
 * application lifecycle phases and provides access to the underlying Spark {@link Service} instance.
 *
 * @param <ConfigT> the app's config type
 */
public interface SparkApplication<ConfigT extends SparkConfigSection> extends Application<ConfigT> {

    /**
     * Returns the app's actual port. The port is only known after the app has been started since a configured value of
     * 0 will cause the server to bind to a random available port.
     *
     * @throws IllegalStateException if the app has not been started
     */
    int port();

    /**
     * Returns the app's underlying Spark {@link Service} instance.
     */
    Service service();
}

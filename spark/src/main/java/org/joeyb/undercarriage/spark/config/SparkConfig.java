package org.joeyb.undercarriage.spark.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;
import org.joeyb.undercarriage.spark.SparkApplication;

/**
 * {@code SparkConfig} is the config container for settings needed by the core {@link SparkApplication}.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableSparkConfig.class)
@JsonSerialize(as = ImmutableSparkConfig.class)
public interface SparkConfig {

    /**
     * Returns the port that the Spark server should run on. If the value is 0, then a random available port will be
     * chosen at runtime.
     */
    @Value.Default
    default int port() {
        return 3000;
    }
}

package org.joeyb.undercarriage.spark.config;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.spark.SparkApplication;

/**
 * {@code SparkConfigSection} defines the minimum required configuration for a {@link SparkApplication}.
 */
public interface SparkConfigSection extends ConfigSection {

    SparkConfig spark();
}

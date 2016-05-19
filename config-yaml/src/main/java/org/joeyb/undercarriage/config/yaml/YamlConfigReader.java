package org.joeyb.undercarriage.config.yaml;

import java.util.Collection;

/**
 * {@code YamlConfigReader} defines the interface for reading the YAML config files that should be used to configure
 * the application.
 */
public interface YamlConfigReader {

    /**
     * Returns the collection of YAML configs. Order matters in the collection. If different configs specify values for
     * the same setting, then the value given by the later config should overwrite the value of the earlier config.
     */
    Collection<String> readConfigs();
}

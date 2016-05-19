package org.joeyb.undercarriage.core.config;

/**
 * {@code ConfigSection} is the base marker interface for each application type and plugin's config. The actual config
 * class for an application will be required (via type constraints) to implement all of the {@code ConfigSection}
 * interfaces used by the app and its enabled plugins.
 */
public interface ConfigSection {
}

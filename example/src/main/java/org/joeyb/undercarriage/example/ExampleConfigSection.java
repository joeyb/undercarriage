package org.joeyb.undercarriage.example;

import org.joeyb.undercarriage.core.config.ConfigSection;

/**
 * {@code ExampleConfigSection} defines custom settings for the {@link ExamplePlugin}.
 */
public interface ExampleConfigSection extends ConfigSection {

    String someSetting();
}

package org.joeyb.undercarriage.jersey.config;

import org.joeyb.undercarriage.core.config.ConfigSection;

/**
 * {@code JerseyConfigSection} is a {@link ConfigSection} that defines the core settings needed for all Jersey-based
 * applications.
 */
public interface JerseyConfigSection extends ConfigSection {

    /**
     * Returns the app's {@link JerseyConfig}.
     */
    JerseyConfig jersey();
}

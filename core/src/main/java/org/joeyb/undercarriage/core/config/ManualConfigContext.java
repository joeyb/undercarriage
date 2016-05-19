package org.joeyb.undercarriage.core.config;

import javax.inject.Inject;

/**
 * {@code ManualConfigContext} is an implementation of {@link ConfigContext} that just returns the config given in its
 * constructor. In general it should only be used for testing and for very simple apps.
 *
 * @param <ConfigT> the app's config type
 */
public class ManualConfigContext<ConfigT extends ConfigSection> implements ConfigContext<ConfigT> {

    private final ConfigT config;

    @Inject
    public ManualConfigContext(ConfigT config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigT config() {
        return config;
    }
}

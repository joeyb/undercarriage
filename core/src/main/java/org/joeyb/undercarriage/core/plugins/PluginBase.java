package org.joeyb.undercarriage.core.plugins;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;

import static java.util.Objects.requireNonNull;

/**
 * {@code PluginBase} provides a base default implementation for {@link Plugin}.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class PluginBase<ConfigT extends ConfigSection> implements Plugin<ConfigT> {

    private final ConfigContext<? extends ConfigT> configContext;

    protected PluginBase(ConfigContext<? extends ConfigT> configContext) {
        this.configContext = requireNonNull(configContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigContext<? extends ConfigT> configContext() {
        return configContext;
    }
}

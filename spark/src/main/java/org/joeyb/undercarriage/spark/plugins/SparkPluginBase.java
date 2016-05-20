package org.joeyb.undercarriage.spark.plugins;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.PluginBase;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import spark.Service;

/**
 * {@code SparkPluginBase} provides a base default implementation for {@link SparkPlugin}.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class SparkPluginBase<ConfigT extends SparkConfigSection>
        extends PluginBase<ConfigT>
        implements SparkPlugin<ConfigT> {

    private final Service service;

    protected SparkPluginBase(ConfigContext<? extends ConfigT> configContext, Service service) {
        super(configContext);

        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service service() {
        return service;
    }
}

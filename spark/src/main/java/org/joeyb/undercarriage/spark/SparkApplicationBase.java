package org.joeyb.undercarriage.spark;

import org.joeyb.undercarriage.core.ApplicationBase;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.utils.Ports;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;

import spark.Service;

/**
 * {@code SparkApplicationBase} provides a base default implementation for the {@link SparkApplication} interface.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class SparkApplicationBase<ConfigT extends SparkConfigSection>
        extends ApplicationBase<ConfigT>
        implements SparkApplication<ConfigT> {

    private final Service service;

    private int port;

    protected SparkApplicationBase(ConfigContext<ConfigT> configContext, Service service) {
        super(configContext);

        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int port() {
        if (!isStarted()) {
            throw new IllegalStateException("The application must be started before we know its port.");
        }

        return port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service service() {
        return service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onConfigure() {
        super.onConfigure();

        port = configContext().config().spark().port();

        // If the port is set to 0, then choose a random available port.
        if (port == 0) {
            port = Ports.availablePort();
        }

        service().port(port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStart() {
        super.onStart();

        service().init();
    }
}

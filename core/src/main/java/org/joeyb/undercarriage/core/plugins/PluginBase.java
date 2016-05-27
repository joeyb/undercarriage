package org.joeyb.undercarriage.core.plugins;

import static java.util.Objects.requireNonNull;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;

/**
 * {@code PluginBase} provides a base default implementation for {@link Plugin}.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class PluginBase<ConfigT extends ConfigSection> implements Plugin<ConfigT> {

    private final ConfigContext<? extends ConfigT> configContext;

    private volatile boolean isConfigured;
    private volatile boolean isStarted;
    private volatile boolean isStopped;

    protected PluginBase(ConfigContext<? extends ConfigT> configContext) {
        this.configContext = requireNonNull(configContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized void configure() {
        if (isConfigured()) {
            throw new IllegalStateException("Plugins can only be configured once.");
        }

        onConfigure();

        isConfigured = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConfigContext<? extends ConfigT> configContext() {
        return configContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isConfigured() {
        return isConfigured;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isStarted() {
        return isStarted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isStopped() {
        return isStopped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized void start() {
        if (isStarted()) {
            throw new IllegalStateException("Plugins can only be started once.");
        }

        onStart();

        isStarted = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized void stop() {
        if (isStopped()) {
            throw new IllegalStateException("Plugins can only be stopped once.");
        }

        if (!isStarted()) {
            throw new IllegalStateException("Plugins must be started before they can be stopped.");
        }

        onStop();

        isStopped = true;
    }

    /**
     * Called by {@link #configure()} and should be used by inheritors to perform configuration tasks. Overrides should
     * be sure to call {@code super.onConfigure()} in order to support default configuration provided by the base
     * classes.
     */
    protected void onConfigure() {

    }

    /**
     * Called by {@link #start()} and should be used by inheritors to perform start tasks. Overrides should be sure to
     * call {@code super.onStart()} in order to support default start tasks provided by the base classes.
     */
    protected void onStart() {

    }

    /**
     * Called by {@link #stop()} and should be used by inheritors to perform stop tasks. Overrides should be sure to
     * call {@code super.onStop()} in order to support default stop tasks provided by the base classes.
     */
    protected void onStop() {

    }
}

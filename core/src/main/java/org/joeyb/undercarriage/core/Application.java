package org.joeyb.undercarriage.core;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.plugins.Plugin;

import java.security.InvalidParameterException;

/**
 * {@code Application} is the base interface for all application types. It defines the core lifecycle phases.
 *
 * @param <ConfigT> the app's config type
 */
public interface Application<ConfigT extends ConfigSection> {

    /**
     * Returns the {@link ConfigContext} implementation used by the application.
     */
    ConfigContext<ConfigT> configContext();

    /**
     * Performs any pre-start configuration. An application can only be configured once. Most apps will not need to call
     * this directly, it'll be automatically executed via {@link #start()}
     */
    void configure();

    /**
     * Returns whether or not {@link #configure()} has been executed for the application.
     */
    boolean isConfigured();

    /**
     * Returns whether or not {@link #start()} has been executed for the application.
     */
    boolean isStarted();

    /**
     * Returns whether or not {@link #stop()} has been executed for the application.
     */
    boolean isStopped();

    /**
     * Returns the instance of the enabled plugin with the given type.
     *
     * @param pluginClass the requested plugin type
     * @param <PluginT> the requested plugin type
     * @throws InvalidParameterException if the requested plugin type is not enabled
     * @return the request plugin instance
     */
    <PluginT extends Plugin<? super ConfigT>> PluginT plugin(Class<PluginT> pluginClass);

    /**
     * Returns all enabled plugins.
     */
    Iterable<Plugin<? super ConfigT>> plugins();

    /**
     * Starts the application. An application can only be started once. By default, {@code start()} automatically calls
     * {@link #configure()}.
     *
     * @throws IllegalStateException if the application is started more than once
     */
    void start();

    /**
     * Stops the application. An application can only be stopped once and must have already been started before it can
     * be stopped.
     *
     * @throws IllegalStateException if the application is stopped more than once or it has not been started
     */
    void stop();
}

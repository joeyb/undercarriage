package org.joeyb.undercarriage.core.plugins;

import com.google.common.collect.ImmutableList;

import org.joeyb.undercarriage.core.Application;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;

/**
 * {@code Plugin} is the base interface for all application plugins. It defines the core lifecycle phases.
 *
 * @param <ConfigT> the app's config type
 */
public interface Plugin<ConfigT extends ConfigSection> {

    /**
     * Returns the {@link ConfigContext} implementation used by the application.
     */
    ConfigContext<? extends ConfigT> configContext();

    /**
     * Performs any pre-start configuration. A plugin can only be configured once. By default the plugins are configured
     * during the application's {@link Application#configure()} phase.
     */
    void configure();

    /**
     * Returns the {@link Class} for each of this plugin's dependencies. This is used to build the dependency graph so
     * that the application can ensure all required plugins are enabled and that they are executed in the correct order.
     */
    default Iterable<Class<? extends Plugin<?>>> dependencies() {
        return ImmutableList.of();
    }

    /**
     * Returns whether or not {@link #configure()} has been executed for the plugin.
     */
    boolean isConfigured();

    /**
     * Returns whether or not {@link #start()} has been executed for the plugin.
     */
    boolean isStarted();

    /**
     * Returns whether or not {@link #stop()} has been executed for the plugin.
     */
    boolean isStopped();

    /**
     * Returns the plugin's {@link PluginPriority}. The priority is used to specify plugin execution order.
     */
    default PluginPriority priority() {
        return PluginPriority.NORMAL;
    }

    /**
     * Starts the plugin. A plugin can only be started once. By default the plugins are started during the application's
     * {@link Application#start()} phase.
     */
    void start();

    /**
     * Stops the plugin. A plugin can only be stopped once and it must have already been started before it can be
     * stopped. By default the plugins are stopped during the application's {@link Application#stop()} phase.
     */
    void stop();
}

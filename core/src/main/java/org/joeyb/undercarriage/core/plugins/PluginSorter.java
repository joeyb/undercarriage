package org.joeyb.undercarriage.core.plugins;

import org.joeyb.undercarriage.core.config.ConfigSection;

/**
 * {@code PluginSorter} defines the interface for sorting plugins for execution.
 */
public interface PluginSorter {

    /**
     * Returns a new {@link Iterable} with a sorted view of the application's plugin.
     *
     * @param plugins the app's enabled plugins
     * @param <T> the app's config type
     */
    <T extends ConfigSection> Iterable<Plugin<? super T>> sort(Iterable<Plugin<? super T>> plugins);
}

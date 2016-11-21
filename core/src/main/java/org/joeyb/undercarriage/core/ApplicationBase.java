package org.joeyb.undercarriage.core;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableList;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.core.plugins.PluginSorter;
import org.joeyb.undercarriage.core.plugins.TopologicalPluginSorter;
import org.joeyb.undercarriage.core.utils.Iterables;
import org.joeyb.undercarriage.core.utils.Suppliers;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@code ApplicationBase} provides a base default implementation for the {@link Application} interface.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class ApplicationBase<ConfigT extends ConfigSection> implements Application<ConfigT> {

    private static final PluginSorter DEFAULT_PLUGIN_SORTER = new TopologicalPluginSorter();

    private final ConfigContext<ConfigT> configContext;
    private final Object lock = new Object();
    private final Supplier<ImmutableList<Plugin<? super ConfigT>>> plugins =
            Suppliers.memoize(this::buildAndSortPlugins);

    private volatile boolean isConfigured;
    private volatile boolean isStarted;
    private volatile boolean isStopped;

    protected ApplicationBase(ConfigContext<ConfigT> configContext) {
        this.configContext = requireNonNull(configContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConfigContext<ConfigT> configContext() {
        return configContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void configure() {
        synchronized (lock) {
            doConfigure();
        }
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
    @SuppressWarnings("unchecked")
    public final <PluginT extends Plugin<? super ConfigT>> Optional<PluginT> optionalPlugin(
            Class<PluginT> pluginClass) {

        Optional<Plugin<? super ConfigT>> plugin = Iterables.stream(plugins())
                .filter(p -> pluginClass.isAssignableFrom(p.getClass()))
                .findFirst();

        // If the plugin was found then we can safely case it to PluginT since we confirmed the class in the filter.
        return plugin.map(p -> (PluginT) p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <PluginT extends Plugin<? super ConfigT>> PluginT plugin(Class<PluginT> pluginClass) {
        return optionalPlugin(pluginClass)
                .orElseThrow(() -> new InvalidParameterException(
                        "The " + pluginClass.getSimpleName() + " plugin is not enabled."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Iterable<Plugin<? super ConfigT>> plugins() {
        return plugins.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <PluginT extends Plugin<? super ConfigT>> Iterable<PluginT> plugins(Class<PluginT> pluginClass) {
        return ImmutableList.copyOf(Iterables.stream(plugins())
                .filter(p -> pluginClass.isAssignableFrom(p.getClass()))
                .map(p -> (PluginT) p)
                .iterator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void start() {
        synchronized (lock) {
            doStart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void stop() {
        synchronized (lock) {
            doStop();
        }
    }

    /**
     * Returns the plugins that are disabled for the application. The main use case for this method is to disable
     * default plugins instead of needing to filter the {@link Iterable} returned by {@link #enabledPlugins()}.
     * Implementors should also call {@code super.disabledPlugins()} and merge its result with theirs before returning
     * in order to support default disabled plugins. For example:
     *
     * <pre>
     * {@code
     * {@literal @}Override
     * protected Iterable<Class<?>> disabledPlugins() {
     *     return Iterables.concat(super.disabledPlugins(), ImmutableList.of(Plugin1.class, Plugin2.class));
     * }
     * }
     * </pre>
     */
    protected Iterable<Class<?>> disabledPlugins() {
        return ImmutableList.of();
    }

    /**
     * Returns the plugins that are enabled for the application. Implementors should also call
     * {@code super.enabledPlugins()} and merge its result with theirs before returning in order to support default
     * plugins. For example:
     *
     * <pre>
     * {@code
     * {@literal @}Override
     * protected Iterable<Plugin<? super ConfigT>> enabledPlugins() {
     *     return Iterables.concat(super.enabledPlugins(), ImmutableList.of(plugin1, plugin2));
     * }
     * }
     * </pre>
     */
    protected Iterable<Plugin<? super ConfigT>> enabledPlugins() {
        return ImmutableList.of();
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

    /**
     * Returns the {@link PluginSorter} to use for ordering plugin execution.
     */
    protected PluginSorter pluginSorter() {
        return DEFAULT_PLUGIN_SORTER;
    }

    private ImmutableList<Plugin<? super ConfigT>> buildAndSortPlugins() {
        final ImmutableList<Class<?>> disabledPlugins = ImmutableList.copyOf(disabledPlugins());

        return ImmutableList.copyOf(pluginSorter().sort(ImmutableList.copyOf(enabledPlugins()).stream()
                .filter(p -> disabledPlugins.stream().noneMatch(dp -> dp.isAssignableFrom(p.getClass())))
                .collect(Collectors.toList())));
    }

    private void doConfigure() {
        if (isConfigured()) {
            throw new IllegalStateException("Applications can only be configured once.");
        }

        plugins().forEach(Plugin::configure);

        onConfigure();

        isConfigured = true;
    }

    private void doStart() {
        if (isStarted()) {
            throw new IllegalStateException("Applications can only be started once.");
        }

        if (!isConfigured()) {
            doConfigure();
        }

        plugins().forEach(Plugin::start);

        onStart();

        isStarted = true;
    }

    private void doStop() {
        if (isStopped()) {
            throw new IllegalStateException("Applications can only be stopped once.");
        }

        if (!isStarted()) {
            throw new IllegalStateException("Applications must be started before they can be stopped.");
        }

        ImmutableList.copyOf(plugins()).reverse().forEach(Plugin::stop);

        onStop();

        isStopped = true;
    }
}

package org.joeyb.undercarriage.core;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.core.plugins.PluginSorter;
import org.joeyb.undercarriage.core.plugins.TopologicalPluginSorter;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * {@code ApplicationBase} provides a base default implementation for the {@link Application} interface.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class ApplicationBase<ConfigT extends ConfigSection> implements Application<ConfigT> {

    private static final PluginSorter DEFAULT_PLUGIN_SORTER = new TopologicalPluginSorter();

    private final ConfigContext<ConfigT> configContext;
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
    public ConfigContext<ConfigT> configContext() {
        return configContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure() {
        if (isConfigured()) {
            throw new IllegalStateException("Applications can only be configured once.");
        }

        plugins().forEach(Plugin::configure);

        isConfigured = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConfigured() {
        return isConfigured;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStopped() {
        return isStopped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <PluginT extends Plugin<? super ConfigT>> PluginT plugin(Class<PluginT> pluginClass) {
        Optional<Plugin<? super ConfigT>> plugin = plugins.get().stream()
                .filter(p -> p.getClass().equals(pluginClass))
                .findFirst();

        // If the plugin was found then we can safely case it to PluginT since we confirmed the class in the filter.
        return plugin.map(p -> (PluginT) p)
                .orElseThrow(() -> new InvalidParameterException(
                        "The " + pluginClass.getSimpleName() + " plugin is not enabled."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Plugin<? super ConfigT>> plugins() {
        return plugins.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (isStarted()) {
            throw new IllegalStateException("Applications can only be started once.");
        }

        configure();

        plugins().forEach(Plugin::start);

        isStarted = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        if (isStopped()) {
            throw new IllegalStateException("Applications can only be stopped once.");
        }

        if (!isStarted()) {
            throw new IllegalStateException("Applications must be started before they can be stopped.");
        }

        ImmutableList.copyOf(plugins()).reverse().forEach(Plugin::stop);

        isStopped = true;
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
     * Returns the {@link PluginSorter} to use for ordering plugin execution.
     */
    protected PluginSorter pluginSorter() {
        return DEFAULT_PLUGIN_SORTER;
    }

    private ImmutableList<Plugin<? super ConfigT>> buildAndSortPlugins() {
        final ImmutableList<Class<?>> disabledPlugins = ImmutableList.copyOf(disabledPlugins());

        return ImmutableList.copyOf(pluginSorter().sort(ImmutableList.copyOf(enabledPlugins()).stream()
                .filter(p -> !disabledPlugins.contains(p.getClass()))
                .collect(Collectors.toList())));
    }
}

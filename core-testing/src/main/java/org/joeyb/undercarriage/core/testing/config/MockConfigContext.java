package org.joeyb.undercarriage.core.testing.config;

import static java.util.Objects.requireNonNull;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import net.jodah.typetools.TypeResolver;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.utils.Suppliers;

import java.util.function.Supplier;

/**
 * {@code MockConfigContext} provides a Mockito-based mockable config instance.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class MockConfigContext<ConfigT extends ConfigSection> implements ConfigContext<ConfigT> {

    private final Supplier<ConfigT> config;

    /**
     * Constructs {@code MockConfigContext} with an entirely mocked config. The mock it set up with Mockito's deep stubs
     * support, so you can mock multiple levels down into your config.
     */
    public MockConfigContext() {
        this.config = Suppliers.memoize(() -> mock(configClass(), RETURNS_DEEP_STUBS));
    }

    /**
     * Constructs {@code MockConfigContext} with a {@link ConfigT} instance to delegate to. The mock config instance
     * returned by {@link #config()} delegates to the given config if no explicit mock value has been specified for the
     * method being called on the mock.
     *
     * <p>The mock returned by {@link #config()} will not have deep stubs. Mockito does not currently support combining
     * mock delegation/forwarding with deep stubs.
     *
     * @param config the config to be delegated to
     */
    public MockConfigContext(ConfigT config) {
        requireNonNull(config);

        this.config = Suppliers.memoize(() -> mock(configClass(), delegatesTo(config)));
    }

    /**
     * Constructs {@code MockConfigContext} with a {@link ConfigContext} instance to delegate to. The mock config
     * instance returned by {@link #config()} delegates to the config returned by the given {@link ConfigContext} if no
     * explicit mock value has been specified for the method being called on the mock.
     *
     * <p>The real config instance is fetched and memoized the first time the mock config is fetched.
     *
     * <p>The mock returned by {@link #config()} will not have deep stubs. Mockito does not currently support combining
     * mock delegation/forwarding with deep stubs.
     *
     * @param configContext the config context to be delegated to
     */
    public MockConfigContext(ConfigContext<ConfigT> configContext) {
        requireNonNull(configContext);

        this.config = Suppliers.memoize(() -> mock(configClass(), delegatesTo(configContext.config())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ConfigT config() {
        return config.get();
    }

    /**
     * Returns the {@link Class} for the app's config. {@link MockConfigContext} must be {@code abstract} for this to
     * work properly due to type erasure.
     */
    @SuppressWarnings("unchecked")
    private Class<ConfigT> configClass() {
        return (Class<ConfigT>) TypeResolver.resolveRawArgument(MockConfigContext.class, getClass());
    }
}

package org.joeyb.undercarriage.grpc.plugins;

import com.google.common.collect.ImmutableList;

import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.PluginBase;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;

/**
 * {@code GrpcPluginBase} provides a base default implementation for {@link GrpcPlugin}.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class GrpcPluginBase<ConfigT extends GrpcConfigSection>
        extends PluginBase<ConfigT>
        implements GrpcPlugin<ConfigT> {

    protected GrpcPluginBase(ConfigContext<? extends ConfigT> configContext) {
        super(configContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ServerServiceDefinition> serverServiceDefinitions() {
        return ImmutableList.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ServerInterceptor> serverInterceptors() {
        return ImmutableList.of();
    }
}

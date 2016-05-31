package org.joeyb.undercarriage.grpc.plugins;

import com.google.common.collect.ImmutableList;

import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

import org.joeyb.undercarriage.core.ApplicationResolver;
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

    protected GrpcPluginBase(ApplicationResolver applicationResolver, ConfigContext<? extends ConfigT> configContext) {
        super(applicationResolver, configContext);
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

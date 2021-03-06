package org.joeyb.undercarriage.grpc.plugins;

import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.grpc.GrpcApplication;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;

/**
 * {@code GrpcPlugin} is the base interface for all {@link GrpcApplication} plugins.
 *
 * @param <ConfigT> the app's config type
 */
public interface GrpcPlugin<ConfigT extends GrpcConfigSection> extends Plugin<ConfigT> {

    /**
     * Returns the {@link ServerServiceDefinition}s that are provided by this plugin. The {@link GrpcApplication} adds
     * them to the {@link Server} during the application start phase.
     */
    Iterable<ServerServiceDefinition> serverServiceDefinitions();

    /**
     * Returns the {@link ServerInterceptor}s that are provided by this plugin. The {@link GrpcApplication} applies them
     * to all {@link ServerServiceDefinition}s (aggregated from the app and all its plugins) during the application
     * start phase.
     */
    Iterable<ServerInterceptor> serverInterceptors();
}

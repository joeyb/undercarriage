package org.joeyb.undercarriage.grpc;

import org.joeyb.undercarriage.core.Application;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;

import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

/**
 * {@code GrpcApplication} is the base interface for all gRPC-based applications. It defines the gRPC-specific
 * application lifecycle phases and provides access to the underlying gRPC {@link Server} instance.
 *
 * @param <ConfigT> the app's config type
 */
public interface GrpcApplication<ConfigT extends GrpcConfigSection> extends Application<ConfigT> {

    int port();

    Server server();

    Iterable<ServerServiceDefinition> serverServiceDefinitions();

    Iterable<ServerServiceDefinition> serverServiceDefinitionsWithInterceptors();

    Iterable<ServerInterceptor> serverInterceptors();
}

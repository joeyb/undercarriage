package org.joeyb.undercarriage.grpc;

import org.joeyb.undercarriage.core.Application;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;

import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

public interface GrpcApplication<ConfigT extends GrpcConfigSection> extends Application<ConfigT> {

    Iterable<ServerServiceDefinition> serverServiceDefinitions();

    Iterable<ServerServiceDefinition> serverServiceDefinitionsWithInterceptors();

    Iterable<ServerInterceptor> serverInterceptors();
}

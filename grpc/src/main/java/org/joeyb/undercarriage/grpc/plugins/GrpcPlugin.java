package org.joeyb.undercarriage.grpc.plugins;

import com.google.common.collect.ImmutableList;

import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;

import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

public interface GrpcPlugin<ConfigT extends GrpcConfigSection> extends Plugin<ConfigT> {

    default Iterable<ServerServiceDefinition> serverServiceDefinitions() {
        return ImmutableList.of();
    }

    default Iterable<ServerInterceptor> serverInterceptors() {
        return ImmutableList.of();
    }
}

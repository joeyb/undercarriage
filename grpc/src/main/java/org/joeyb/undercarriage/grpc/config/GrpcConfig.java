package org.joeyb.undercarriage.grpc.config;

import org.immutables.value.Value;
import org.joeyb.undercarriage.grpc.GrpcApplication;

/**
 * {@code GrpcConfig} is the config container for settings needed by the core {@link GrpcApplication}.
 */
@Value.Immutable
public interface GrpcConfig {

    /**
     * Returns the port that the gRPC server should run on. If the value is 0, then a random available port will be
     * chosen at runtime.
     */
    int port();
}

package org.joeyb.undercarriage.grpc.config;

import org.immutables.value.Value;

@Value.Immutable
public interface GrpcConfig {

    int port();
}

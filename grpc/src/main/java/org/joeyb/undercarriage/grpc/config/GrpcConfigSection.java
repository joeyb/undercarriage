package org.joeyb.undercarriage.grpc.config;

import org.joeyb.undercarriage.core.config.ConfigSection;

public interface GrpcConfigSection extends ConfigSection {

    GrpcConfig grpc();
}

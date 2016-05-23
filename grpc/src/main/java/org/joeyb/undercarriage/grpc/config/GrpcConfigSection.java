package org.joeyb.undercarriage.grpc.config;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.grpc.GrpcApplication;

/**
 * {@code GrpcConfigSection} defines the minimum required configuration for a {@link GrpcApplication}.
 */
public interface GrpcConfigSection extends ConfigSection {

    GrpcConfig grpc();
}

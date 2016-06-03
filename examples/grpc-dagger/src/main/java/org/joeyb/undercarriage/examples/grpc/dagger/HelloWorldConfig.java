package org.joeyb.undercarriage.examples.grpc.dagger;

import org.immutables.value.Value;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;

@Value.Immutable
public interface HelloWorldConfig extends GrpcConfigSection {

}

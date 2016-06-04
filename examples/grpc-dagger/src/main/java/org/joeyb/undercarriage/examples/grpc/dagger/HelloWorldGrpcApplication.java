package org.joeyb.undercarriage.examples.grpc.dagger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import io.grpc.ServerServiceDefinition;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.grpc.GrpcApplicationBase;

import javax.inject.Inject;

public class HelloWorldGrpcApplication extends GrpcApplicationBase<HelloWorldConfig> {

    @Inject
    public HelloWorldGrpcApplication(ConfigContext<HelloWorldConfig> configContext) {
        super(configContext);
    }

    @Override
    protected Iterable<ServerServiceDefinition> enabledServerServiceDefinitions() {
        return Iterables.concat(
                super.enabledServerServiceDefinitions(),
                ImmutableList.of(GreeterGrpc.bindService(new GreeterImpl())));
    }
}

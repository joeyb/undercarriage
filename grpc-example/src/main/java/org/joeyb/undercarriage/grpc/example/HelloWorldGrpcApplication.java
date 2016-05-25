package org.joeyb.undercarriage.grpc.example;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.grpc.GrpcApplicationBase;

import io.grpc.ServerServiceDefinition;

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
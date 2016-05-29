package org.joeyb.undercarriage.grpc.example.dagger;

import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.grpc.config.ImmutableGrpcConfig;
import org.joeyb.undercarriage.grpc.example.HelloWorldConfig;
import org.joeyb.undercarriage.grpc.example.ImmutableHelloWorldConfig;

import javax.inject.Singleton;

@Module
public class HelloWorldGrpcApplicationConfigModule {

    @Provides
    @Singleton
    ConfigContext<HelloWorldConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableHelloWorldConfig.builder()
                        .grpc(ImmutableGrpcConfig.builder()
                                      .port(0)
                                      .build())
                        .build());
    }
}

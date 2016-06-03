package org.joeyb.undercarriage.examples.grpc.dagger.dagger;

import dagger.Module;
import dagger.Provides;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.examples.grpc.dagger.HelloWorldConfig;
import org.joeyb.undercarriage.examples.grpc.dagger.ImmutableHelloWorldConfig;
import org.joeyb.undercarriage.grpc.config.ImmutableGrpcConfig;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    ConfigContext<HelloWorldConfig> provideConfigContext() {
        return new ManualConfigContext<>(
                ImmutableHelloWorldConfig.builder()
                        .grpc(
                                ImmutableGrpcConfig.builder()
                                        .port(0)
                                        .build())
                        .build());
    }
}

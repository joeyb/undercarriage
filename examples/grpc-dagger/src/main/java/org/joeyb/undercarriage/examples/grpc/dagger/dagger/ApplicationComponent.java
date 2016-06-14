package org.joeyb.undercarriage.examples.grpc.dagger.dagger;

import dagger.Component;

import org.joeyb.undercarriage.examples.grpc.dagger.HelloWorldGrpcApplication;

import javax.inject.Singleton;

@Component(modules = ConfigContextModule.class)
@Singleton
public interface ApplicationComponent {

    HelloWorldGrpcApplication application();
}

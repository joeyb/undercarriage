package org.joeyb.undercarriage.examples.grpc.dagger.dagger;

import dagger.Component;

import org.joeyb.undercarriage.examples.grpc.dagger.HelloWorldGrpcApplication;

import javax.inject.Singleton;

@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {

    HelloWorldGrpcApplication application();
}

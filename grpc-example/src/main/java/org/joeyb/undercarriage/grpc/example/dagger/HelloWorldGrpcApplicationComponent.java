package org.joeyb.undercarriage.grpc.example.dagger;

import dagger.Component;

import org.joeyb.undercarriage.grpc.example.HelloWorldGrpcApplication;

import javax.inject.Singleton;

@Component(modules = HelloWorldGrpcApplicationConfigModule.class)
@Singleton
public interface HelloWorldGrpcApplicationComponent {

    HelloWorldGrpcApplication application();
}

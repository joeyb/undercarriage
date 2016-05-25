package org.joeyb.undercarriage.grpc.example.dagger;

import org.joeyb.undercarriage.grpc.example.HelloWorldGrpcApplication;

import dagger.Component;

import javax.inject.Singleton;

@Component(modules = HelloWorldGrpcApplicationConfigModule.class)
@Singleton
public interface HelloWorldGrpcApplicationComponent {

    HelloWorldGrpcApplication application();
}

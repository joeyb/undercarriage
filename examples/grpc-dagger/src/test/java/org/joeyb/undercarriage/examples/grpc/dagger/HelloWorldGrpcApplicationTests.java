package org.joeyb.undercarriage.examples.grpc.dagger;

import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.joeyb.undercarriage.examples.grpc.dagger.dagger.DaggerApplicationComponent;
import org.junit.Test;

import java.util.UUID;

public class HelloWorldGrpcApplicationTests {

    @Test
    public void sayHelloEndpointReturnsExpectedResponse() {
        final String name = UUID.randomUUID().toString();

        HelloWorldGrpcApplication application = DaggerApplicationComponent.create().application();

        application.start();

        final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", application.port())
                .usePlaintext(true)
                .build();

        final GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        HelloResponse response = blockingStub.sayHello(request);

        application.stop();

        assertThat(response.getMessage()).isEqualTo("Hello " + name);
    }
}

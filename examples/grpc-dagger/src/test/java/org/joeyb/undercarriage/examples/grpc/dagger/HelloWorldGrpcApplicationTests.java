package org.joeyb.undercarriage.examples.grpc.dagger;

import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.joeyb.undercarriage.examples.grpc.dagger.dagger.DaggerApplicationComponent;
import org.joeyb.undercarriage.examples.grpc.dagger.dagger.MockConfigContextModule;
import org.joeyb.undercarriage.grpc.testing.GrpcApplicationTestRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

public class HelloWorldGrpcApplicationTests {

    @Rule
    public final GrpcApplicationTestRule<HelloWorldConfig, HelloWorldGrpcApplication> applicationTestRule =
            new GrpcApplicationTestRule<>(
                    () -> DaggerApplicationComponent.builder()
                            .configContextModule(new MockConfigContextModule())
                            .build()
                            .application());

    @Test
    public void sayHelloEndpointReturnsExpectedResponse() {
        final String name = UUID.randomUUID().toString();

        final ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", applicationTestRule.application().port())
                .usePlaintext(true)
                .build();

        final GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder()
                .setName(name)
                .build();

        HelloResponse response = blockingStub.sayHello(request);

        assertThat(response.getMessage()).isEqualTo("Hello " + name);
    }
}

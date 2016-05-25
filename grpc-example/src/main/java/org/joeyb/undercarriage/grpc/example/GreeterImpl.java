package org.joeyb.undercarriage.grpc.example;

import io.grpc.stub.StreamObserver;

public class GreeterImpl implements GreeterGrpc.Greeter {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse reply = HelloResponse.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}

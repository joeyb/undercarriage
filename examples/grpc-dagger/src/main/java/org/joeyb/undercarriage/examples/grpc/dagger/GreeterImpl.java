package org.joeyb.undercarriage.examples.grpc.dagger;

import io.grpc.stub.StreamObserver;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse reply = HelloResponse.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}

# `grpc`

The `grpc` library provides support for building web applications based on the [gRPC](http://grpc.io/) framework.

To see an example of a working gRPC app, check out [`examples/grpc-dagger`](../examples/grpc-dagger/).

### `GrpcApplication`

The `GrpcApplication` interface defines the gRPC-specific application methods. The `GrpcApplication#server()` method returns the underlying gRPC `Server` instance.

Most applications should inherit from the `GrpcApplicationBase` abstract implementation. It provides some common default implementations for the underlying application interfaces.

### `GrpcPlugin`

The `GrpcPlugin` interface defines the gRPC-specific plugin methods.

Most plugins should inherit from the `GrpcPluginBase` abstract implementation. It provides some common default implementations for the underlying plugin interfaces.

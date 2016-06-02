# `jersey`

The `jersey` library provides support for building web applications based on the [Jersey](https://jersey.java.net/) framework.

To see an example of a working Jersey app, check out [`jersey-example`](../jersey-example/).

It's important to note that Jersey is strongly tied to its dependency injection framework, HK2. Because of this, the `undercarriage` Jersey implementation cannot be DI framework agnostic.

### `JerseyApplication`

The `JerseyApplication` interface defines the Jersey-specific application methods. The `JerseyApplication#resourceConfig()` method returns the underlying Jersey `ResourceConfig` instance, which is used to configure the web server and add routes.

Most applications should inherit from the `JerseyApplicationBase` abstract implementation. It provides some common default implementations for the underlying application interfaces.

### `JerseyPlugin`

The `JerseyPlugin` interface defines the Jersey-specific plugin methods. Similar to `JerseyApplication`, it also provides access to the underlying Jersey `ResourceConfig` instance via its `JerseyPlugin#resourceConfig()` method.

Most plugins should inherit from the `JerseyPluginBase` abstract implementation. It provides some common default implementations for the underlying plugin interfaces.

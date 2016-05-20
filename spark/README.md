# `spark`

The `spark` library provides support for building web applications based on the [Spark](http://sparkjava.com/) framework.

To see an example of a working Spark app, check out [`spark-example`](../spark-example/).

### `SparkApplication`

The `SparkApplication` interface defines the Spark-specific application methods. The `SparkApplication#service()` method returns the underlying Spark `Service` instance, which is used to configure the web server and add routes. It's important to note that Spark is set up to automatically start its web server as soon as a route is added, so all of the route configuration should be done during the application's `start` lifecycle phase (and not during `configure`).

Most applications should inherit from the `SparkApplicationBase` abstract implementation. It provides some common default implementations for the underlying application interfaces.

### `SparkPlugin`

The `SparkPlugin` interface defines the Spark-specific plugin methods. Similar to `SparkApplication`, it also provides access to the underlying Spark `Service` instance via its `SparkPlugin#service()` method, and its route configuration should also be done during the plugin's `start` lifecycle phase.

Most plugins should inherit from the `SparkPluginBase` abstract implementation. It provides some common default implementations for the underlying plugin interfaces.

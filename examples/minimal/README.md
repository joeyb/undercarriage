# Minimal Example

This example demonstrates a bare minimum `undercarriage`-based application. All dependencies are set up manually (i.e. no DI framework), the configuration is specified statically via the `ManualConfigContext`, and console logging is configured via the `slf4j-simple` library.

The [immutables](http://immutables.org) library is used to automatically generate the builder for `MinimalConfig`.

When executed, the `MinimalApplication` will log messages for the `Application#configure()`, `Application#start()`, and `Application#stop()` lifecycle phases.

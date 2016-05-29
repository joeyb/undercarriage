# `core`

The `core` library provides the foundational framework for all `undercarriage`-based applications.

### `Application<ConfigT>`

All `undercarriage`-based applications implement the underlying `Application<ConfigT>` interface. It defines the common application lifecycle phases, as well as methods for accessing the app's config, plugins, etc. A basic application goes through the following lifecycle:

```
configure -> start -> stop
```

### `ApplicationBase<ConfigT>`

The `ApplicationBase<ConfigT>` class provides a default implementation of the `Application<ConfigT>` interface. Unless your application has very specific needs, it will probably extend from `ApplicationBase<ConfigT>` (or one of its child classes).

`ApplicationBase<ConfigT>` handles managing the app's current lifecycle phase and ensures that each is executed in the correct order. It provides `protected` method extension points for adding your app's custom functionality (i.e. `onConfigure`, `onStart`, `onStop`). For convenience, when `start()` is called on an app, if it hasn't already been configured then the configure phase is automatically executed first. Most apps can therefore skip calling `configure()` as an explicit step and just call `start()`.

### `ConfigSection`

The `ConfigT` used throughout the `undercarriage` framework is the config type for your application. The base `Application<ConfigT>` interface defines that `ConfigT` is extended from `ConfigSection`. The framework uses generic constraints with interfaces derived from `ConfigSection` in order to define minimal config requirements for each of the application and plugin types. For example, take a hypothetical application derived type called `WidgetApplication` that's defined as:

```java
public interface WidgetApplication<ConfigT extends WidgetConfigSection> extends Application<ConfigT> {
    // ...
}
```

The generic constraints force the implementors to at least implement the `WidgetConfigSection` interface on their application's config. A hypothetical `WidgetApplication`-based app that also uses a few plugins may end up with a config that looks like this:

```java
public interface MyWidgetApplicationConfig extends WidgetConfigSection,
                                                   SomePluginConfigSection,
                                                   OtherPluginConfigSection {

    String someSetting();
    
    String otherSetting();
}
```



### `ConfigContext<ConfigT>`

Instead of exposing the `ConfigT` instance directly on the application, the framework uses the `ConfigContext<ConfigT>` abstraction layer in order to support a variety of different config providers. The `core` library also provides a basic implementation, `ManualConfigContext<ConfigT>`, that simply takes the `ConfigT` instance in its constructor and returns it via that `config()` method. Implementations provided in other packages support reading the configs from different sources.

### `Plugin<ConfigT>`

The framework supports hooking into the application's lifecycle phases via plugins. The base `Plugin<ConfigT>` interface defines methods for the same lifecycle phases as `Application<ConfigT>` and each application sub-type will have a similar corresponding plugin sub-type.

### `PluginBase<ConfigT>`

The `PluginBase<ConfigT>` class provides a default implementation of `Plugin<ConfigT>`, similar to the relationship between `ApplicationBase<ConfigT>` and `Application<ConfigT>`. Unless your plugin has very specific needs, it will probably extend from `PluginBase<ConfigT>` (or one of its child classes).

# `undercarriage`

[![Build Status](https://travis-ci.org/joeyb/undercarriage.svg?branch=master)](https://travis-ci.org/joeyb/undercarriage) [![codecov](https://codecov.io/gh/joeyb/undercarriage/branch/master/graph/badge.svg)](https://codecov.io/gh/joeyb/undercarriage)

`undercarriage` is a general-purpose Java application framework. The project's main design goals are:

- **Testability** - Aim for near 100% branch coverage with fast and accurate tests.
- **Extensibility** - Where possible, provide extension points for swapping out interface implementations and hooking into the application's lifecycle. This is mainly accomplished via Dependency Injection and the plugin infrastructure.
- **Documentation** - All public and protected APIs should be thoroughly documented. Each library should have higher-level usage docs and each application type should have end-to-end tutorial-style docs.
- **Modern Tooling** - Take advantage of the latest features provided by Java 8 and modern libraries. Prefer modern best practices with newer libraries over legacy code to support older (but maybe more popular) libraries.
- **DI Framework Agnostic** - The core code should never be tied to a particular dependency injection framework. It uses the JSR-330 annotations where needed. The framework is designed so that it will play nicely with a compile-time DI framework like [Dagger 2](http://google.github.io/dagger/).

There is a cost to being DI framework agnostic. JSR-330 does not currently standardize dependency configuration, so we don't currently provide a way for base application types or plugins to automatically register the dependencies that they provide. This means that you'll need to manually setup the full DI configuration for your particular framework. The documentation for each base application type and each plugin will define their required dependencies.

The [core](core/) library provides the foundational framework for building an application.

To see some real examples, check out the [`examples`](examples/) directory.

### Libraries

| Library | Package |
| ------- | ------- |
| [config-yaml](config-yaml/) | `"org.joeyb.undercarriage:config-yaml:$undercarriageVersion"` |
| [core](core/) | `"org.joeyb.undercarriage:core:$undercarriageVersion"` |
| [grpc](grpc/) | `"org.joeyb.undercarriage:grpc:$undercarriageVersion"` |
| [jersey](jersey/) | `"org.joeyb.undercarriage:jersey:$undercarriageVersion"` |
| [spark](spark/) | `"org.joeyb.undercarriage:spark:$undercarriageVersion"` |

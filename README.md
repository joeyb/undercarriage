# `undercarriage`

[![Build Status](https://travis-ci.org/joeyb/undercarriage.svg?branch=master)](https://travis-ci.org/joeyb/undercarriage) [![codecov](https://codecov.io/gh/joeyb/undercarriage/branch/master/graph/badge.svg)](https://codecov.io/gh/joeyb/undercarriage)

`undercarriage` is a general-purpose Java application framework built on top of [Dagger 2](http://google.github.io/dagger/). The project's main design goals are:

- **Testability** - Aim for near 100% branch coverage with fast and accurate tests.
- **Extensibility** - Where possible, provide extension points for swapping out interface implementations and hooking into the application's lifecycle. This is mainly accomplished via Dependency Injection and the plugin infrastructure.
- **Documentation** - All public and protected APIs should be thoroughly documented. Each library should have higher-level usage docs and each application type should have end-to-end tutorial-style docs.
- **Modern Tooling** - Take advantage of the latest features provided by Java 8 and modern libraries. Prefer modern best practices with newer libraries over legacy code to support older (but maybe more popular) libraries.

Basing the framework on Dagger places some limitations on the libraries that `undercarriage` can play nicely with. For example, [Jersey](https://jersey.java.net/) is strongly tied to its DI framework ([HK2](https://hk2.java.net/)). It was not built with support for using a different DI framework, especially not one that forces the entire dependency graph to be built at compile time, so it is not a good candidate for integrating with `undercarriage`.

The [core](core/) library provides the foundational framework for building an application.

### Libraries

| Library | Package |
| ------- | ------- |
| [config-yaml](config-yaml/) | `"org.joeyb.undercarriage:config-yaml:$undercarriageVersion"` |
| [core](core/) | `"org.joeyb.undercarriage:core:$undercarriageVersion"` |
| [grpc](grpc/) | `"org.joeyb.undercarriage:grpc:$undercarriageVersion"` |
| [spark](spark/) | `"org.joeyb.undercarriage:spark:$undercarriageVersion"` |

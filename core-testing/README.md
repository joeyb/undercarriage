# `core-testing`

The `core-testing` library provides tools to assist with testing `undercarriage` applications.

### `ApplicationTestRule<ConfigT, AppT>`

The `ApplicationTestRule` class is a JUnit [Rule](https://github.com/junit-team/junit4/wiki/Rules) that takes care of starting and stopping the `undercarriage` app for each test.

### `MockConfigContext<ConfigT>`

The `MockConfigContext` class is an implementation of `ConfigContext` that returns a Mockito-based mockable config instance. It provides constructors that allow you to either create a full mock with deep stubs, or a normal mock that delegates to the config returned by another given `ConfigContext` instance.

This allows you to use Mockito's mocking methods to programmatically change out config values on a per-test basis.

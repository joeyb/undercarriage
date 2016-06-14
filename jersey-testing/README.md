# `jersey-testing`

The `jersey-testing` library provides tools to assist with testing [Jersey](https://jersey.java.net/)-based `undercarriage` applications.

### `JerseyApplicationTestRule<ConfigT, AppT>`

The `JerseyApplicationTestRule` class is a JUnit [Rule](https://github.com/junit-team/junit4/wiki/Rules) that takes care of starting and stopping the `undercarriage` app for each test. If the config provided by the app's `ConfigContext` is a mock then the rule will automatically handle setting the app to run on a random port in order to avoid port conflicts. The app's base URI will be set to `http://localhost:0/`. It will also be configured to *not* join the server's thread.

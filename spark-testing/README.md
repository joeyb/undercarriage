# `spark-testing`

The `spark-testing` library provides tools to assist with testing [Spark](http://sparkjava.com/)-based `undercarriage` applications.

### `SparkApplicationTestRule<ConfigT, AppT>`

The `SparkApplicationTestRule` class is a JUnit [Rule](https://github.com/junit-team/junit4/wiki/Rules) that takes care of starting and stopping the `undercarriage` app for each test. If the config provided by the app's `ConfigContext` is a mock then the rule will automatically handle setting the app to run on a random port in order to avoid port conflicts.

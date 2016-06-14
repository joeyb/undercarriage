package org.joeyb.undercarriage.spark.testing;

import static org.assertj.core.api.Assertions.assertThat;

import org.immutables.value.Value;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.core.testing.config.MockConfigContext;
import org.joeyb.undercarriage.core.utils.Ports;
import org.joeyb.undercarriage.spark.SparkApplicationBase;
import org.joeyb.undercarriage.spark.config.ImmutableSparkConfig;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import org.junit.Rule;
import org.junit.Test;

import spark.Service;

public class SparkApplicationTestRuleTests {

    public static class NonMockConfig {

        private static final int PORT = Ports.availablePort();

        private static final TestSparkApplicationConfig CONFIG = ImmutableTestSparkApplicationConfig.builder()
                .spark(ImmutableSparkConfig.builder()
                              .port(PORT)
                              .build())
                .build();

        @Rule
        public final SparkApplicationTestRule<TestSparkApplicationConfig, TestSparkApplication> rule =
                new SparkApplicationTestRule<>(() -> new TestSparkApplication(new ManualConfigContext<>(CONFIG)));

        @Test
        public void portShouldRemainUnchanged() {
            assertThat(rule.application().configContext().config().spark().port())
                    .isEqualTo(PORT);

            assertThat(rule.application().port())
                    .isEqualTo(PORT);
        }
    }

    public static class MockWithDeepStubsConfig {

        @Rule
        public final SparkApplicationTestRule<TestSparkApplicationConfig, TestSparkApplication> rule =
                new SparkApplicationTestRule<>(() -> new TestSparkApplication(
                        new MockConfigContext<TestSparkApplicationConfig>() { }));

        @Test
        public void portShouldBeSetToRandom() {
            assertThat(rule.application().configContext().config().spark().port())
                    .isEqualTo(0);

            assertThat(rule.application().port())
                    .isNotEqualTo(0);
        }
    }

    public static class MockWithoutDeepStubsConfig {

        private static final int PORT = -1;

        private static final TestSparkApplicationConfig CONFIG = ImmutableTestSparkApplicationConfig.builder()
                .spark(ImmutableSparkConfig.builder()
                              .port(PORT)
                              .build())
                .build();

        @Rule
        public final SparkApplicationTestRule<TestSparkApplicationConfig, TestSparkApplication> rule =
                new SparkApplicationTestRule<>(() -> new TestSparkApplication(
                        new MockConfigContext<TestSparkApplicationConfig>(CONFIG) { }));

        @Test
        public void portShouldBeSetToRandom() {
            assertThat(rule.application().configContext().config().spark().port())
                    .isEqualTo(0);

            assertThat(rule.application().port())
                    .isNotEqualTo(PORT)
                    .isNotEqualTo(0);
        }
    }

    @Value.Immutable
    public interface TestSparkApplicationConfig extends SparkConfigSection {

    }

    private static class TestSparkApplication extends SparkApplicationBase<TestSparkApplicationConfig> {

        TestSparkApplication(ConfigContext<TestSparkApplicationConfig> configContext) {
            super(configContext, Service.ignite());
        }
    }
}

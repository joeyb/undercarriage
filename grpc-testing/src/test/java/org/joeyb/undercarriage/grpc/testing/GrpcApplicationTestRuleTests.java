package org.joeyb.undercarriage.grpc.testing;

import static org.assertj.core.api.Assertions.assertThat;

import org.immutables.value.Value;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.core.testing.config.MockConfigContext;
import org.joeyb.undercarriage.core.utils.Ports;
import org.joeyb.undercarriage.grpc.GrpcApplicationBase;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;
import org.joeyb.undercarriage.grpc.config.ImmutableGrpcConfig;
import org.junit.Rule;
import org.junit.Test;

public class GrpcApplicationTestRuleTests {

    public static class NonMockConfig {

        private static final int PORT = Ports.availablePort();

        private static final TestGrpcApplicationConfig CONFIG = ImmutableTestGrpcApplicationConfig.builder()
                .grpc(ImmutableGrpcConfig.builder()
                              .port(PORT)
                              .build())
                .build();

        @Rule
        public final GrpcApplicationTestRule<TestGrpcApplicationConfig, TestGrpcApplication> rule =
                new GrpcApplicationTestRule<>(() -> new TestGrpcApplication(new ManualConfigContext<>(CONFIG)));

        @Test
        public void portShouldRemainUnchanged() {
            assertThat(rule.application().configContext().config().grpc().port())
                    .isEqualTo(PORT);

            assertThat(rule.application().port())
                    .isEqualTo(PORT);
        }
    }

    public static class MockWithDeepStubsConfig {

        @Rule
        public final GrpcApplicationTestRule<TestGrpcApplicationConfig, TestGrpcApplication> rule =
                new GrpcApplicationTestRule<>(() -> new TestGrpcApplication(
                        new MockConfigContext<TestGrpcApplicationConfig>() { }));

        @Test
        public void portShouldBeSetToRandom() {
            assertThat(rule.application().configContext().config().grpc().port())
                    .isEqualTo(0);

            assertThat(rule.application().port())
                    .isNotEqualTo(0);
        }
    }

    public static class MockWithoutDeepStubsConfig {

        private static final int PORT = -1;

        private static final TestGrpcApplicationConfig CONFIG = ImmutableTestGrpcApplicationConfig.builder()
                .grpc(ImmutableGrpcConfig.builder()
                              .port(PORT)
                              .build())
                .build();

        @Rule
        public final GrpcApplicationTestRule<TestGrpcApplicationConfig, TestGrpcApplication> rule =
                new GrpcApplicationTestRule<>(() -> new TestGrpcApplication(
                        new MockConfigContext<TestGrpcApplicationConfig>(CONFIG) { }));

        @Test
        public void portShouldBeSetToRandom() {
            assertThat(rule.application().configContext().config().grpc().port())
                    .isEqualTo(0);

            assertThat(rule.application().port())
                    .isNotEqualTo(PORT)
                    .isNotEqualTo(0);
        }
    }

    @Value.Immutable
    public interface TestGrpcApplicationConfig extends GrpcConfigSection {

    }

    private static class TestGrpcApplication extends GrpcApplicationBase<TestGrpcApplicationConfig> {

        TestGrpcApplication(ConfigContext<TestGrpcApplicationConfig> configContext) {
            super(configContext);
        }
    }
}

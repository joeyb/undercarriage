package org.joeyb.undercarriage.jersey.testing;

import static org.assertj.core.api.Assertions.assertThat;

import org.glassfish.hk2.api.Factory;
import org.immutables.value.Value;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.core.testing.config.MockConfigContext;
import org.joeyb.undercarriage.core.utils.Ports;
import org.joeyb.undercarriage.jersey.JerseyApplicationBase;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.joeyb.undercarriage.jersey.hk2.JerseyApplicationBaseBinder;
import org.junit.Rule;
import org.junit.Test;

public class JerseyApplicationTestRuleTests {

    public static class NonMockConfig {

        private static final int PORT = Ports.availablePort();

        private static final TestJerseyApplicationConfig CONFIG = ImmutableTestJerseyApplicationConfig.builder()
                .jersey(ImmutableJerseyConfig.builder()
                              .baseUri("http://localhost:" + PORT)
                              .joinServerThread(false)
                              .build())
                .build();

        @Rule
        public final JerseyApplicationTestRule<TestJerseyApplicationConfig, TestJerseyApplication> rule =
                new JerseyApplicationTestRule<>(() -> new TestJerseyApplication(TestConfigContextFactory.class));

        @Test
        public void portShouldRemainUnchanged() {
            assertThat(rule.application().configContext().config().jersey().baseUri())
                    .isEqualTo("http://localhost:" + PORT);

            assertThat(rule.application().port())
                    .isEqualTo(PORT);
        }

        private static class TestConfigContextFactory implements Factory<ConfigContext<TestJerseyApplicationConfig>> {

            @Override
            public ConfigContext<TestJerseyApplicationConfig> provide() {
                return new ManualConfigContext<>(CONFIG);
            }

            @Override
            public void dispose(ConfigContext<TestJerseyApplicationConfig> instance) {

            }
        }
    }

    public static class MockWithDeepStubsConfig {

        @Rule
        public final JerseyApplicationTestRule<TestJerseyApplicationConfig, TestJerseyApplication> rule =
                new JerseyApplicationTestRule<>(() -> new TestJerseyApplication(TestConfigContextFactory.class));

        @Test
        public void portShouldBeSetToRandom() {
            assertThat(rule.application().configContext().config().jersey().baseUri())
                    .isEqualTo("http://localhost:0");

            assertThat(rule.application().port())
                    .isNotEqualTo(0);
        }

        private static class TestConfigContextFactory implements Factory<ConfigContext<TestJerseyApplicationConfig>> {

            @Override
            public ConfigContext<TestJerseyApplicationConfig> provide() {
                return new MockConfigContext<TestJerseyApplicationConfig>() { };
            }

            @Override
            public void dispose(ConfigContext<TestJerseyApplicationConfig> instance) {

            }
        }
    }

    public static class MockWithoutDeepStubsConfig {

        private static final int PORT = -1;

        private static final TestJerseyApplicationConfig CONFIG = ImmutableTestJerseyApplicationConfig.builder()
                .jersey(ImmutableJerseyConfig.builder()
                                .baseUri("http://localhost:" + PORT)
                                .joinServerThread(false)
                                .build())
                .build();

        @Rule
        public final JerseyApplicationTestRule<TestJerseyApplicationConfig, TestJerseyApplication> rule =
                new JerseyApplicationTestRule<>(() -> new TestJerseyApplication(TestConfigContextFactory.class));

        @Test
        public void portShouldBeSetToRandom() {
            assertThat(rule.application().configContext().config().jersey().baseUri())
                    .isEqualTo("http://localhost:0");

            assertThat(rule.application().port())
                    .isNotEqualTo(PORT)
                    .isNotEqualTo(0);
        }

        private static class TestConfigContextFactory implements Factory<ConfigContext<TestJerseyApplicationConfig>> {

            @Override
            public ConfigContext<TestJerseyApplicationConfig> provide() {
                return new MockConfigContext<TestJerseyApplicationConfig>(CONFIG) { };
            }

            @Override
            public void dispose(ConfigContext<TestJerseyApplicationConfig> instance) {

            }
        }
    }

    @Value.Immutable
    public interface TestJerseyApplicationConfig extends JerseyConfigSection {

    }

    private static class TestJerseyApplication extends JerseyApplicationBase<TestJerseyApplicationConfig> {

        TestJerseyApplication(
                Class<? extends Factory<ConfigContext<TestJerseyApplicationConfig>>> configContextFactory) {
            super(new JerseyApplicationBaseBinder<>(configContextFactory));
        }
    }
}

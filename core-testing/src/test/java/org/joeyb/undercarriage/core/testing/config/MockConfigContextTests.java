package org.joeyb.undercarriage.core.testing.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.immutables.value.Value;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.core.utils.Randoms;
import org.junit.Test;

import java.util.UUID;

public class MockConfigContextTests {

    @Test
    public void mockConfigIsMockableWithDeepStubs() {
        final String mockChildString = UUID.randomUUID().toString();
        final int mockInt = Randoms.randInt(200000, 300000);
        final String mockString = UUID.randomUUID().toString();

        ConfigContext<MockConfigContextConfig> configContext = new MockConfigContext<MockConfigContextConfig>() { };

        when(configContext.config().childConfig().someChildString()).thenReturn(mockChildString);
        when(configContext.config().someInt()).thenReturn(mockInt);
        when(configContext.config().someString()).thenReturn(mockString);

        assertThat(configContext.config().childConfig().someChildString()).isEqualTo(mockChildString);
        assertThat(configContext.config().someInt()).isEqualTo(mockInt);
        assertThat(configContext.config().someString()).isEqualTo(mockString);
    }

    @Test
    public void constructorThrowsIfGivenConfigContextIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new MockConfigContext<MockConfigContextConfig>(null) { });
    }

    @Test
    public void decoratedConfigIsMockable() {
        final String someChildString = UUID.randomUUID().toString();
        final int someInt = Randoms.randInt(0, 100000);
        final String someString = UUID.randomUUID().toString();

        final int mockInt = Randoms.randInt(200000, 300000);
        final String mockString = UUID.randomUUID().toString();

        ConfigContext<MockConfigContextConfig> configContext =
                new MockConfigContext<MockConfigContextConfig>(
                        new ManualConfigContext<>(
                                ImmutableMockConfigContextConfig.builder()
                                        .childConfig(
                                                ImmutableChildConfig.builder()
                                                        .someChildString(someChildString)
                                                        .build())
                                        .someInt(someInt)
                                        .someString(someString)
                                        .build())) { };

        assertThat(configContext.config().childConfig().someChildString()).isEqualTo(someChildString);
        assertThat(configContext.config().someInt()).isEqualTo(someInt);
        assertThat(configContext.config().someString()).isEqualTo(someString);

        when(configContext.config().someInt()).thenReturn(mockInt);
        when(configContext.config().someString()).thenReturn(mockString);

        assertThat(configContext.config().childConfig().someChildString()).isEqualTo(someChildString);
        assertThat(configContext.config().someInt()).isEqualTo(mockInt);
        assertThat(configContext.config().someString()).isEqualTo(mockString);
    }

    @Test
    public void decoratedConfigIsMemoized() {
        ConfigContext<MockConfigContextConfig> configContext =
                new MockConfigContext<MockConfigContextConfig>(
                        new ExplodingConfigContext<>(
                                ImmutableMockConfigContextConfig.builder()
                                        .childConfig(
                                                ImmutableChildConfig.builder()
                                                        .someChildString(UUID.randomUUID().toString())
                                                        .build())
                                        .someInt(Randoms.randInt(0, 100000))
                                        .someString(UUID.randomUUID().toString())
                                        .build())) { };

        // The underlying ExplodingConfigContext will fail if its config is not memoized.
        configContext.config();
        configContext.config();
    }

    @Value.Immutable
    public interface MockConfigContextConfig extends ConfigSection {

        ChildConfig childConfig();

        int someInt();

        String someString();
    }

    @Value.Immutable
    public interface ChildConfig {

        String someChildString();
    }

    private static class ExplodingConfigContext<ConfigT extends ConfigSection> extends ManualConfigContext<ConfigT> {

        private volatile boolean configAlreadyFetched;

        ExplodingConfigContext(ConfigT config) {
            super(config);
        }

        @Override
        public ConfigT config() {
            if (configAlreadyFetched) {
                fail("Config should be memoized");
            }

            configAlreadyFetched = true;

            return super.config();
        }
    }
}

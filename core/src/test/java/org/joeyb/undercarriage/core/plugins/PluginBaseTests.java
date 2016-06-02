package org.joeyb.undercarriage.core.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.CountDownLatch;

public class PluginBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public ApplicationResolver applicationResolver;

    @Mock
    public ConfigContext<ConfigSection> configContext;

    @Test
    public void givenApplicationResolverIsReturnedByGetter() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        assertThat(plugin.applicationResolver())
                .isEqualTo(applicationResolver);
    }

    @Test
    public void givenConfigContextIsReturnedByGetter() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        assertThat(plugin.configContext())
                .isEqualTo(configContext);
    }

    @Test
    public void pluginConstructionThrowsForNullApplicationResolver() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new MockPlugin(null, configContext));
    }

    @Test
    public void pluginConstructionThrowsForNullConfigContext() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new MockPlugin(applicationResolver, null));
    }

    @Test
    public void configureThrowsIfExecutedTwice() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        plugin.configure();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(plugin::configure);
    }

    @Test
    public void configureCallsOnConfigure() throws InterruptedException {
        CountDownLatch onConfigureLatch = new CountDownLatch(1);

        MockPlugin plugin = new MockPlugin(applicationResolver, configContext) {
            @Override
            protected void onConfigure() {
                onConfigureLatch.countDown();
            }
        };

        plugin.configure();

        onConfigureLatch.await();
    }

    @Test
    public void isConfiguredIsSetByConfigure() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        assertThat(plugin.isConfigured()).isEqualTo(false);

        plugin.configure();

        assertThat(plugin.isConfigured()).isEqualTo(true);
    }

    @Test
    public void isStartedIsSetByStart() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        assertThat(plugin.isStarted()).isEqualTo(false);

        plugin.start();

        assertThat(plugin.isStarted()).isEqualTo(true);
    }

    @Test
    public void isStoppedIsSetByStop() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        assertThat(plugin.isStopped()).isEqualTo(false);

        plugin.start();
        plugin.stop();

        assertThat(plugin.isStopped()).isEqualTo(true);
    }

    @Test
    public void startCallsOnStart() throws InterruptedException {
        CountDownLatch onStartLatch = new CountDownLatch(1);

        MockPlugin plugin = new MockPlugin(applicationResolver, configContext) {
            @Override
            protected void onStart() {
                onStartLatch.countDown();
            }
        };

        plugin.start();

        onStartLatch.await();
    }

    @Test
    public void startThrowsIfExecutedTwice() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        plugin.start();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(plugin::start);
    }

    @Test
    public void stopCallsOnStop() throws InterruptedException {
        CountDownLatch onStopLatch = new CountDownLatch(1);

        MockPlugin plugin = new MockPlugin(applicationResolver, configContext) {
            @Override
            protected void onStop() {
                onStopLatch.countDown();
            }
        };

        plugin.start();
        plugin.stop();

        onStopLatch.await();
    }

    @Test
    public void stopThrowsIfExecutedTwice() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        plugin.start();

        plugin.stop();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(plugin::stop);
    }

    @Test
    public void stopThrowsIfApplicationNeverStarted() {
        MockPlugin plugin = new MockPlugin(applicationResolver, configContext);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(plugin::stop);
    }

    private static class MockPlugin extends PluginBase<ConfigSection> {

        MockPlugin(ApplicationResolver applicationResolver, ConfigContext<ConfigSection> configContext) {
            super(applicationResolver, configContext);
        }
    }
}

package org.joeyb.undercarriage.spark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.utils.Randoms;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import spark.Service;

public class SparkApplicationBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    public MockSparkConfigContext configContext;

    @Test
    public void portThrowsIfNotStarted() {
        Service service = mock(Service.class);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(application::port);
    }

    @Test
    public void nonZeroPortIsSetByStart() {
        final int port = Randoms.randInt(1000, 5000);

        Service service = mock(Service.class);

        when(configContext.config().spark().port()).thenReturn(port);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        assertThat(application.port()).isEqualTo(port);

        verify(service).port(eq(application.port()));
    }

    @Test
    public void randomPortIsSetForZeroPortByStart() {
        Service service = mock(Service.class);

        when(configContext.config().spark().port()).thenReturn(0);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        assertThat(application.port()).isNotEqualTo(0);

        verify(service).port(eq(application.port()));
    }

    @Test
    public void serviceInitIsCalledByStart() {
        Service service = mock(Service.class);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        verify(service).init();
    }

    @Test
    public void serviceAwaitInitializationIsCalledByStart() {
        Service service = mock(Service.class);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        verify(service).awaitInitialization();
    }

    private static class MockSparkApplication extends SparkApplicationBase<SparkConfigSection> {

        MockSparkApplication(ConfigContext<SparkConfigSection> configContext, Service service) {
            super(configContext, service);
        }
    }

    private interface MockSparkConfigContext extends ConfigContext<SparkConfigSection> {

    }
}

package org.joeyb.undercarriage.spark;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.utils.Randoms;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import spark.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class SparkApplicationBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    public SparkConfigContext configContext;

    @Test
    public void portThrowsIfNotStarted() {
        Service service = PowerMockito.mock(Service.class);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(application::port);
    }

    @Test
    public void nonZeroPortIsSetByStart() {
        final int port = Randoms.randInt(1000, 5000);

        Service service = PowerMockito.mock(Service.class);

        when(configContext.config().spark().port()).thenReturn(port);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        assertThat(application.port()).isEqualTo(port);

        verify(service).port(eq(application.port()));
    }

    @Test
    public void randomPortIsSetForZeroPortByStart() {
        Service service = PowerMockito.mock(Service.class);

        when(configContext.config().spark().port()).thenReturn(0);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        assertThat(application.port()).isNotEqualTo(0);

        verify(service).port(eq(application.port()));
    }

    @Test
    public void serviceInitIsCalledByStart() {
        Service service = PowerMockito.mock(Service.class);

        MockSparkApplication application = new MockSparkApplication(configContext, service);

        application.start();

        verify(service).init();
    }

    private interface SparkConfigContext extends ConfigContext<SparkConfigSection> {

    }

    private static class MockSparkApplication extends SparkApplicationBase<SparkConfigSection> {

        MockSparkApplication(ConfigContext<SparkConfigSection> configContext, Service service) {
            super(configContext, service);
        }
    }
}

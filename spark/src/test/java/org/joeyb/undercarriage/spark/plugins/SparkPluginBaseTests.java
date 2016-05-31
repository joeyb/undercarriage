package org.joeyb.undercarriage.spark.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import spark.Service;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class SparkPluginBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public ApplicationResolver applicationResolver;

    @Mock
    public MockSparkConfigContext configContext;

    @Test
    public void serviceReturnsGivenService() {
        Service service = PowerMockito.mock(Service.class);

        SparkPlugin<SparkConfigSection> plugin = new SparkPluginBase<SparkConfigSection>(
                applicationResolver,
                configContext,
                service) { };

        assertThat(plugin.service()).isEqualTo(service);
    }

    private interface MockSparkConfigContext extends ConfigContext<SparkConfigSection> {

    }
}

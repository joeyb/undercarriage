package org.joeyb.undercarriage.spark.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.spark.config.SparkConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import spark.Service;

public class SparkPluginBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public ApplicationResolver applicationResolver;

    @Mock
    public MockSparkConfigContext configContext;

    @Test
    public void serviceReturnsGivenService() {
        Service service = mock(Service.class);

        SparkPlugin<SparkConfigSection> plugin = new SparkPluginBase<SparkConfigSection>(
                applicationResolver,
                configContext,
                service) { };

        assertThat(plugin.service()).isEqualTo(service);
    }

    private interface MockSparkConfigContext extends ConfigContext<SparkConfigSection> {

    }
}

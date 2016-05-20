package org.joeyb.undercarriage.spark.plugins;

import org.joeyb.undercarriage.spark.SparkConfigContext;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class SparkPluginBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public SparkConfigContext configContext;

    @Test
    public void serviceReturnsGivenService() {
        Service service = PowerMockito.mock(Service.class);

        SparkPlugin<SparkConfigSection> plugin = new SparkPluginBase<SparkConfigSection>(configContext, service) { };

        assertThat(plugin.service()).isEqualTo(service);
    }
}

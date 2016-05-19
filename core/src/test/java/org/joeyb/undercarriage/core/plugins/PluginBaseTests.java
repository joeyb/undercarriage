package org.joeyb.undercarriage.core.plugins;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;

public class PluginBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public ConfigContext<ConfigSection> configContext;

    @Test
    public void givenConfigContextIsReturnedByGetter() {
        MockPlugin plugin = new MockPlugin(configContext);

        assertThat(plugin.configContext())
                .isEqualTo(configContext);
    }

    private static class MockPlugin extends PluginBase<ConfigSection> {

        MockPlugin(ConfigContext<ConfigSection> configContext) {
            super(configContext);
        }
    }
}

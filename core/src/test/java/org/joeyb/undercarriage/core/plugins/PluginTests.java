package org.joeyb.undercarriage.core.plugins;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

public class PluginTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = CALLS_REAL_METHODS)
    public Plugin<ConfigSection> plugin;


    @Test
    public void pluginHasZeroDefaultDependencies() {
        Iterable<Class<? extends Plugin<?>>> dependencies = plugin.dependencies();

        assertThat(dependencies)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    public void pluginDefaultsToNormalPriority() {
        assertThat(plugin.priority()).isEqualTo(PluginPriority.NORMAL);
    }
}

package org.joeyb.undercarriage.core.plugins;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PluginTests {

    @Test
    public void pluginHasZeroDefaultDependencies() {
        MockPlugin plugin = new MockPlugin();

        Iterable<Class<? extends Plugin<?>>> dependencies = plugin.dependencies();

        assertThat(dependencies)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    public void pluginDefaultsToNormalPriority() {
        MockPlugin plugin = new MockPlugin();

        assertThat(plugin.priority()).isEqualTo(PluginPriority.NORMAL);
    }
}

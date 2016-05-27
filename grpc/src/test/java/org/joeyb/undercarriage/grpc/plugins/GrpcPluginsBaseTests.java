package org.joeyb.undercarriage.grpc.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class GrpcPluginsBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public MockGrpcConfigContext configContext;

    @Test
    public void serverInterceptorsIsEmptyByDefault() {
        MockGrpcPlugin plugin = new MockGrpcPlugin(configContext);

        assertThat(plugin.serverInterceptors())
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void serverServiceDefinitionsIsEmptyByDefault() {
        MockGrpcPlugin plugin = new MockGrpcPlugin(configContext);

        assertThat(plugin.serverServiceDefinitions())
                .isNotNull()
                .isEmpty();
    }

    private interface MockGrpcConfigContext extends ConfigContext<GrpcConfigSection> {

    }

    private static class MockGrpcPlugin extends GrpcPluginBase<GrpcConfigSection> {

        MockGrpcPlugin(ConfigContext<? extends GrpcConfigSection> configContext) {
            super(configContext);
        }
    }
}

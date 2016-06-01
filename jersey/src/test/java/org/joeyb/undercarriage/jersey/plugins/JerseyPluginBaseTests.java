package org.joeyb.undercarriage.jersey.plugins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.jersey.JerseyApplication;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class JerseyPluginBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public JerseyApplication<?> application;

    @Mock
    public ApplicationResolver applicationResolver;

    @Mock
    public MockJerseyConfigContext configContext;

    @Before
    public void setUpApplicationResolver() {
        when(applicationResolver.application(eq(JerseyApplication.class))).thenReturn(application);
    }

    @Test
    public void resourceConfigIsReturnedFromTheApplicationResolver() {
        ResourceConfig resourceConfig = new ResourceConfig();

        when(application.resourceConfig()).thenReturn(resourceConfig);

        MockJerseyPlugin plugin = new MockJerseyPlugin(applicationResolver, configContext);

        assertThat(plugin.resourceConfig()).isEqualTo(resourceConfig);
    }

    @Test
    public void serviceLocatorIsReturnedFromTheApplicationResolver() {
        ServiceLocator serviceLocator = mock(ServiceLocator.class);

        when(application.serviceLocator()).thenReturn(serviceLocator);

        MockJerseyPlugin plugin = new MockJerseyPlugin(applicationResolver, configContext);

        assertThat(plugin.serviceLocator()).isEqualTo(serviceLocator);
    }

    private interface MockJerseyConfigContext extends ConfigContext<JerseyConfigSection> {

    }

    private static class MockJerseyPlugin extends JerseyPluginBase<JerseyConfigSection> {

        MockJerseyPlugin(
                ApplicationResolver applicationResolver,
                ConfigContext<? extends JerseyConfigSection> configContext) {
            super(applicationResolver, configContext);
        }
    }
}

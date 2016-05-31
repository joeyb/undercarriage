package org.joeyb.undercarriage.jersey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jetty.server.Server;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.utils.GenericClass;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.junit.Before;
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

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Server.class)
public class JerseyApplicationBaseTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    public MockJerseyConfigContext configContext;

    private ResourceConfig resourceConfig;

    @Before
    public void setUpResourceConfig() {
        resourceConfig = new ResourceConfig();
    }

    @Test
    public void constructorWithBinderBuildsValidServiceLocatorAndSetsConfigContextAndResourceConfig() {
        Binder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bind(configContext).to(new GenericClass<ConfigContext<JerseyConfigSection>>() { }.getGenericClass());
                bind(resourceConfig).to(ResourceConfig.class);
            }
        };

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(binder) { };

        assertThat(application.serviceLocator()).isNotNull();
        assertThat(application.configContext()).isEqualTo(configContext);

        ConfigContext<JerseyConfigSection> configContextFromServiceLocator = application.serviceLocator()
                .getService(new GenericClass<ConfigContext<JerseyConfigSection>>() { }.getGenericClass());

        assertThat(configContextFromServiceLocator).isEqualTo(configContext);

        assertThat(application.resourceConfig()).isEqualTo(resourceConfig);
    }

    @Test
    public void constructorWithServiceLocatorReturnsGivenServiceLocatorAndSetsConfigContextAndResourceConfig() {
        ServiceLocator serviceLocator = getMockServiceLocator();

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(serviceLocator) { };

        assertThat(application.serviceLocator()).isEqualTo(serviceLocator);
        assertThat(application.configContext()).isEqualTo(configContext);

        assertThat(application.resourceConfig()).isEqualTo(resourceConfig);
    }

    @Test
    public void portThrowsIfNotStarted() {
        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) { };

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(application::port);
    }

    @Test
    public void startCreatesAndStartsJerseyServerWithoutJoin() throws Exception {
        final String baseUri = "http://localhost:0";

        Server mockServer = PowerMockito.mock(Server.class);

        when(configContext.config().jersey().baseUri()).thenReturn(baseUri);
        when(configContext.config().jersey().joinServerThread()).thenReturn(false);

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) {
                    @Override
                    Server createServer(URI createServerBaseUri) {
                        assertThat(createServerBaseUri.toString()).isEqualTo(baseUri);

                        return mockServer;
                    }
                };

        application.start();

        verify(mockServer).start();
        verify(mockServer, never()).join();
    }

    @Test
    public void startCreatesAndStartsJerseyServerWithJoin() throws Exception {
        final String baseUri = "http://localhost:0";

        Server mockServer = PowerMockito.mock(Server.class);

        when(configContext.config().jersey().baseUri()).thenReturn(baseUri);
        when(configContext.config().jersey().joinServerThread()).thenReturn(true);

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) {
                    @Override
                    Server createServer(URI createServerBaseUri) {
                        assertThat(createServerBaseUri.toString()).isEqualTo(baseUri);

                        return mockServer;
                    }
                };

        application.start();

        verify(mockServer).start();
        verify(mockServer).join();
    }

    private ServiceLocator getMockServiceLocator() {
        return getMockServiceLocator(b -> { });
    }

    private ServiceLocator getMockServiceLocator(Consumer<AbstractBinder> binderConsumer) {
        Binder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                binderConsumer.accept(this);

                bind(configContext).to(new GenericClass<ConfigContext<JerseyConfigSection>>() { }.getGenericClass());
                bind(resourceConfig).to(ResourceConfig.class);
            }
        };

        return ServiceLocatorUtilities.bind(UUID.randomUUID().toString(), binder);
    }

    private interface MockJerseyConfigContext extends ConfigContext<JerseyConfigSection> {

    }
}

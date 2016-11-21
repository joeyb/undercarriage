package org.joeyb.undercarriage.jersey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jetty.server.Server;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.utils.GenericClass;
import org.joeyb.undercarriage.core.utils.Randoms;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

public class JerseyApplicationBaseTests {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    public MockJerseyConfigContext configContext;

    private LateBoundApplicationResolver applicationResolver;
    private ResourceConfig resourceConfig;

    @Before
    public void setUpApplicationResolverAndResourceConfig() {
        applicationResolver = new LateBoundApplicationResolver();
        resourceConfig = new ResourceConfig();
    }

    @Test
    public void constructorWithBinderBuildsValidServiceLocatorAndSetsConfigContextAndResourceConfig() {
        Binder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bind(applicationResolver).to(ApplicationResolver.class).to(LateBoundApplicationResolver.class);
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

        ApplicationResolver applicationResolverFromServiceLocator = application.serviceLocator()
                .getService(ApplicationResolver.class);

        assertThat(applicationResolverFromServiceLocator.application()).isEqualTo(application);
    }

    @Test
    public void constructorWithServiceLocatorReturnsGivenServiceLocatorAndSetsConfigContextAndResourceConfig() {
        ServiceLocator serviceLocator = getMockServiceLocator();

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(serviceLocator) { };

        assertThat(application.serviceLocator()).isEqualTo(serviceLocator);
        assertThat(application.configContext()).isEqualTo(configContext);

        assertThat(application.resourceConfig()).isEqualTo(resourceConfig);

        ApplicationResolver applicationResolverFromServiceLocator = application.serviceLocator()
                .getService(ApplicationResolver.class);

        assertThat(applicationResolverFromServiceLocator.application()).isEqualTo(application);
    }

    @Test
    public void portThrowsIfNotStarted() {
        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) { };

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(application::port);
    }

    @Test
    public void portReturnsPortFromServerUri() {
        final int port = Randoms.randInt(10000, 30000);

        final String baseUri = "http://localhost:" + port;
        final URI mockServerUri = URI.create(baseUri);

        Server mockServer = mock(Server.class);

        when(mockServer.getURI()).thenReturn(mockServerUri);
        when(configContext.config().jersey().baseUri()).thenReturn(baseUri);

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) {
                    @Override
                    Server createServer(URI createServerBaseUri) {
                        return mockServer;
                    }
                };

        application.start();

        assertThat(application.port())
                .isEqualTo(port);
    }

    @Test
    public void startCreatesAndStartsJerseyServerWithoutJoin() throws Exception {
        final String baseUri = "http://localhost:0";

        Server mockServer = mock(Server.class);

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

        Server mockServer = mock(Server.class);

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

    @Test
    public void stopStopsJerseyServer() throws Exception {
        final String baseUri = "http://localhost:0";

        Server mockServer = mock(Server.class);

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

        verify(mockServer, never()).stop();

        application.stop();

        verify(mockServer).stop();
    }

    @Test
    public void interruptExceptionAfterJoinIsCaughtAndStartIsCompleted() throws Exception {
        final String baseUri = "http://localhost:0";

        Server mockServer = mock(Server.class);

        when(configContext.config().jersey().baseUri()).thenReturn(baseUri);
        when(configContext.config().jersey().joinServerThread()).thenReturn(true);
        doThrow(InterruptedException.class).when(mockServer).join();

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

        assertThat(application.isStarted()).isTrue();
    }

    @Test
    public void startCreatesAndStartsRealJerseyServer() throws Exception {
        final String baseUri = "http://localhost:0";

        when(configContext.config().jersey().baseUri()).thenReturn(baseUri);
        when(configContext.config().jersey().joinServerThread()).thenReturn(false);

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) { };

        application.start();

        assertThat(application.port()).isPositive();

        application.stop();
    }

    @Test
    public void exceptionDuringJerseyStopIsCaughtAndStopIsCompleted() throws Exception {
        final String baseUri = "http://localhost:0";

        Server mockServer = mock(Server.class);

        when(configContext.config().jersey().baseUri()).thenReturn(baseUri);
        when(configContext.config().jersey().joinServerThread()).thenReturn(true);
        doThrow(RuntimeException.class).when(mockServer).stop();

        JerseyApplication<JerseyConfigSection> application =
                new JerseyApplicationBase<JerseyConfigSection>(getMockServiceLocator()) {
                    @Override
                    Server createServer(URI createServerBaseUri) {
                        assertThat(createServerBaseUri.toString()).isEqualTo(baseUri);

                        return mockServer;
                    }
                };

        application.start();
        application.stop();

        assertThat(application.isStopped()).isTrue();
    }

    private ServiceLocator getMockServiceLocator() {
        return getMockServiceLocator(b -> { });
    }

    private ServiceLocator getMockServiceLocator(Consumer<AbstractBinder> binderConsumer) {
        Binder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                binderConsumer.accept(this);

                bind(applicationResolver).to(ApplicationResolver.class).to(LateBoundApplicationResolver.class);
                bind(configContext).to(new GenericClass<ConfigContext<JerseyConfigSection>>() { }.getGenericClass());
                bind(resourceConfig).to(ResourceConfig.class);
            }
        };

        return ServiceLocatorUtilities.bind(UUID.randomUUID().toString(), binder);
    }

    private interface MockJerseyConfigContext extends ConfigContext<JerseyConfigSection> {

    }
}

package org.joeyb.undercarriage.grpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;

import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.plugins.Plugin;
import org.joeyb.undercarriage.core.utils.Ports;
import org.joeyb.undercarriage.core.utils.Randoms;
import org.joeyb.undercarriage.grpc.config.GrpcConfigSection;
import org.joeyb.undercarriage.grpc.plugins.GrpcPlugin;
import org.joeyb.undercarriage.grpc.plugins.GrpcPluginBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GrpcApplicationBaseTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public ApplicationResolver applicationResolver;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    public MockGrpcConfigContext configContext;

    @Mock
    public ServerBuilder<?> serverBuilder;

    @Mock
    public Server server;

    @Before
    public void setupMockServer() {
        when(serverBuilder.build()).thenReturn(server);
    }

    @Test
    public void enabledServerInterceptorsDefaultsToEmpty() {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        Iterable<ServerInterceptor> serverInterceptors = application.enabledServerInterceptors();

        assertThat(serverInterceptors)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void enabledServerServiceDefinitionsDefaultsToEmpty() {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        Iterable<ServerServiceDefinition> serverServiceDefinitions = application.enabledServerServiceDefinitions();

        assertThat(serverServiceDefinitions)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void portThrowsIfNotStarted() {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(application::port);
    }

    @Test
    public void portIsPassedToServerBuilderAndReturnedFromServer() {
        final int port = Randoms.randInt(1000, 5000);

        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        when(configContext.config().grpc().port()).thenReturn(port);
        when(server.getPort()).thenReturn(port);

        application.start();

        assertThat(application.configuredPort()).isEqualTo(port);
        assertThat(application.port()).isEqualTo(port);
    }

    @Test
    public void serverThrowsIfNotStarted() {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(application::server);
    }

    @Test
    public void serverReturnsBuiltServerAfterStart() {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        application.start();

        assertThat(application.server()).isEqualTo(server);
    }

    @Test
    public void serverStartIsCalledByStart() throws IOException {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        application.start();

        verify(server).start();
    }

    @Test
    public void serverShutdownNowIsCalledByStop() {
        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder);

        application.start();
        application.stop();

        verify(server).shutdownNow();
    }

    @Test
    public void serverServiceDefinitionsIncludesApplicationAndPluginDefinitions() {
        Iterable<ServerServiceDefinition> applicationDefinitions = ImmutableList.of(
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build(),
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build());

        Iterable<ServerServiceDefinition> pluginDefinitions = ImmutableList.of(
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build(),
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build());

        GrpcPlugin<GrpcConfigSection> plugin = new GrpcPluginBase<GrpcConfigSection>(
                applicationResolver,
                configContext) {
            @Override
            public Iterable<ServerServiceDefinition> serverServiceDefinitions() {
                return pluginDefinitions;
            }
        };

        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder) {
            @Override
            protected Iterable<Plugin<? super GrpcConfigSection>> enabledPlugins() {
                return ImmutableList.of(plugin);
            }

            @Override
            protected Iterable<ServerServiceDefinition> enabledServerServiceDefinitions() {
                return applicationDefinitions;
            }
        };

        assertThat(application.serverServiceDefinitions())
                .containsAll(applicationDefinitions)
                .containsAll(pluginDefinitions);
    }

    @Test
    public void serverInterceptorsIncludesApplicationAndPluginInterceptors() {
        Iterable<ServerInterceptor> applicationInterceptors = ImmutableList.of(
                mock(ServerInterceptor.class),
                mock(ServerInterceptor.class));

        Iterable<ServerInterceptor> pluginInterceptors = ImmutableList.of(
                mock(ServerInterceptor.class),
                mock(ServerInterceptor.class));

        GrpcPlugin<GrpcConfigSection> plugin = new GrpcPluginBase<GrpcConfigSection>(
                applicationResolver,
                configContext) {
            @Override
            public Iterable<ServerInterceptor> serverInterceptors() {
                return pluginInterceptors;
            }
        };

        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder) {
            @Override
            protected Iterable<Plugin<? super GrpcConfigSection>> enabledPlugins() {
                return ImmutableList.of(plugin);
            }

            @Override
            protected Iterable<ServerInterceptor> enabledServerInterceptors() {
                return applicationInterceptors;
            }
        };

        assertThat(application.serverInterceptors())
                .containsAll(applicationInterceptors)
                .containsAll(pluginInterceptors);
    }

    @Test
    public void serverServiceDefinitionsWithInterceptsIncludesApplicationAndPluginDefinitions() {
        Iterable<ServerServiceDefinition> applicationDefinitions = ImmutableList.of(
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build(),
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build());

        Iterable<ServerServiceDefinition> pluginDefinitions = ImmutableList.of(
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build(),
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build());

        Iterable<ServerInterceptor> applicationInterceptors = ImmutableList.of(
                mock(ServerInterceptor.class),
                mock(ServerInterceptor.class));

        Iterable<ServerInterceptor> pluginInterceptors = ImmutableList.of(
                mock(ServerInterceptor.class),
                mock(ServerInterceptor.class));

        GrpcPlugin<GrpcConfigSection> plugin = new GrpcPluginBase<GrpcConfigSection>(
                applicationResolver,
                configContext) {
            @Override
            public Iterable<ServerServiceDefinition> serverServiceDefinitions() {
                return pluginDefinitions;
            }

            @Override
            public Iterable<ServerInterceptor> serverInterceptors() {
                return pluginInterceptors;
            }
        };

        Collection<ServerServiceDefinition> interceptedServerServiceDefinitions = new LinkedList<>();

        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder) {
            @Override
            protected Iterable<Plugin<? super GrpcConfigSection>> enabledPlugins() {
                return ImmutableList.of(plugin);
            }

            @Override
            protected Iterable<ServerServiceDefinition> enabledServerServiceDefinitions() {
                return applicationDefinitions;
            }

            @Override
            protected Iterable<ServerInterceptor> enabledServerInterceptors() {
                return applicationInterceptors;
            }

            @Override
            ServerServiceDefinition applyServiceInterceptor(ServerServiceDefinition serverServiceDefinition,
                                                            List<ServerInterceptor> serverInterceptors) {
                // Make sure all interceptors are applied.
                assertThat(serverInterceptors)
                        .containsAll(applicationInterceptors)
                        .containsAll(pluginInterceptors);

                interceptedServerServiceDefinitions.add(serverServiceDefinition);

                return serverServiceDefinition;
            }
        };

        application.serverServiceDefinitionsWithInterceptors();

        // Make sure all ServerServiceDefinitions have the interceptors applied.
        assertThat(interceptedServerServiceDefinitions)
                .containsAll(applicationDefinitions)
                .containsAll(pluginDefinitions);
    }

    @Test
    public void serverBuilderAddServiceIsCalledForEachDefinitionWithInterceptorsByStart() {
        Iterable<ServerServiceDefinition> applicationDefinitions = ImmutableList.of(
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build(),
                ServerServiceDefinition.builder(UUID.randomUUID().toString()).build());

        Iterable<ServerInterceptor> applicationInterceptors = ImmutableList.of(
                mock(ServerInterceptor.class),
                mock(ServerInterceptor.class));

        Collection<ServerServiceDefinition> interceptedServerServiceDefinitions = new LinkedList<>();

        MockGrpcApplication application = new MockGrpcApplication(configContext, serverBuilder) {

            @Override
            protected Iterable<ServerServiceDefinition> enabledServerServiceDefinitions() {
                return applicationDefinitions;
            }

            @Override
            protected Iterable<ServerInterceptor> enabledServerInterceptors() {
                return applicationInterceptors;
            }

            @Override
            ServerServiceDefinition applyServiceInterceptor(ServerServiceDefinition serverServiceDefinition,
                                                            List<ServerInterceptor> serverInterceptors) {
                // Make sure all interceptors are applied.
                assertThat(serverInterceptors)
                        .containsAll(applicationInterceptors);

                ServerServiceDefinition interceptedServerServiceDefinition =
                        ServerServiceDefinition.builder(UUID.randomUUID().toString()).build();

                interceptedServerServiceDefinitions.add(interceptedServerServiceDefinition);

                return interceptedServerServiceDefinition;
            }
        };

        application.start();

        for (ServerServiceDefinition serverServiceDefinition : interceptedServerServiceDefinitions) {
            verify(serverBuilder).addService(eq(serverServiceDefinition));
        }
    }

    @Test
    public void startCreatesAndStartsRealGrpcServer() {
        final int port = Ports.availablePort();

        when(configContext.config().grpc().port()).thenReturn(port);

        GrpcApplication<GrpcConfigSection> application =
                new GrpcApplicationBase<GrpcConfigSection>(configContext) { };

        application.start();

        assertThat(application.port()).isEqualTo(port);
        assertThat(application.isStarted()).isTrue();

        application.stop();
    }

    private static class MockGrpcApplication extends GrpcApplicationBase<GrpcConfigSection> {

        private final ServerBuilder<?> serverBuilder;

        private int configuredPort;

        MockGrpcApplication(ConfigContext<GrpcConfigSection> configContext, ServerBuilder<?> serverBuilder) {
            super(configContext);

            this.serverBuilder = serverBuilder;
        }

        int configuredPort() {
            return configuredPort;
        }

        @Override
        ServerBuilder<?> createServerBuilder(int port) {
            configuredPort = port;

            return serverBuilder;
        }
    }

    private interface MockGrpcConfigContext extends ConfigContext<GrpcConfigSection> {

    }
}

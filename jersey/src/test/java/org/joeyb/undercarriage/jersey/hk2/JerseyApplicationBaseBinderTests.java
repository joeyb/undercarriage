package org.joeyb.undercarriage.jersey.hk2;

import static org.assertj.core.api.Assertions.assertThat;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.server.ResourceConfig;
import org.immutables.value.Value;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ManualConfigContext;
import org.joeyb.undercarriage.jersey.config.ImmutableJerseyConfig;
import org.joeyb.undercarriage.jersey.config.JerseyConfigSection;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class JerseyApplicationBaseBinderTests {

    private ServiceLocator serviceLocator;

    @Before
    public void setUpServiceLocator() {
        serviceLocator = ServiceLocatorUtilities.bind(
                new JerseyApplicationBaseBinder<>(MockConfigContextFactory.class));
    }

    @Test
    public void applicationResolverInstanceIsBoundToApplicationResolverAsSingleton() {
        ApplicationResolver applicationResolver1 = serviceLocator.getService(ApplicationResolver.class);
        ApplicationResolver applicationResolver2 = serviceLocator.getService(ApplicationResolver.class);

        assertThat(applicationResolver1)
                .isNotNull()
                .isEqualTo(applicationResolver2);
    }

    @Test
    public void applicationResolverInstanceIsBoundToLateBoundApplicationResolverAsSingleton() {
        ApplicationResolver applicationResolver = serviceLocator.getService(ApplicationResolver.class);
        LateBoundApplicationResolver lateBoundApplicationResolver1 =
                serviceLocator.getService(LateBoundApplicationResolver.class);
        LateBoundApplicationResolver lateBoundApplicationResolver2 =
                serviceLocator.getService(LateBoundApplicationResolver.class);

        assertThat(lateBoundApplicationResolver1)
                .isNotNull()
                .isEqualTo(applicationResolver)
                .isEqualTo(lateBoundApplicationResolver2);
    }

    @Test
    public void configContextInstanceIsBoundToConfigContextMockConfigAsSingleton() {
        ConfigContext<MockConfig> configContext1 = serviceLocator.getService(
                new TypeLiteral<ConfigContext<MockConfig>>() { }.getRawType());
        ConfigContext<MockConfig> configContext2 = serviceLocator.getService(
                new TypeLiteral<ConfigContext<MockConfig>>() { }.getRawType());

        assertThat(configContext1)
                .isNotNull()
                .isEqualTo(configContext2);
    }

    @Test
    public void configContextInstanceIsBoundToConfigContextJerseyConfigSectionAsSingleton() {
        ConfigContext<? extends JerseyConfigSection> configContext1 = serviceLocator.getService(
                new TypeLiteral<ConfigContext<JerseyConfigSection>>() { }.getRawType());
        ConfigContext<? extends JerseyConfigSection> configContext2 = serviceLocator.getService(
                new TypeLiteral<ConfigContext<JerseyConfigSection>>() { }.getRawType());

        assertThat(configContext1)
                .isNotNull()
                .isEqualTo(configContext2);
    }

    @Test
    public void resourceConfigInstanceIsBoundToResourceConfigAsSingleton() {
        ResourceConfig resourceConfig1 = serviceLocator.getService(ResourceConfig.class);
        ResourceConfig resourceConfig2 = serviceLocator.getService(ResourceConfig.class);

        assertThat(resourceConfig1)
                .isNotNull()
                .isEqualTo(resourceConfig2);
    }

    @Value.Immutable
    interface MockConfig extends JerseyConfigSection {

        String value();
    }

    private static class MockConfigContextFactory implements Factory<ConfigContext<MockConfig>> {

        @Override
        public ConfigContext<MockConfig> provide() {
            return new ManualConfigContext<>(
                    ImmutableMockConfig.builder()
                            .jersey(ImmutableJerseyConfig.builder()
                                            .baseUri(UUID.randomUUID().toString())
                                            .joinServerThread(true)
                                            .build())
                            .value(UUID.randomUUID().toString())
                            .build());
        }

        @Override
        public void dispose(ConfigContext<MockConfig> instance) {

        }
    }
}

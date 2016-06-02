package org.joeyb.undercarriage.jersey.hk2;

import static org.assertj.core.api.Assertions.assertThat;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.server.ResourceConfig;
import org.joeyb.undercarriage.core.ApplicationResolver;
import org.joeyb.undercarriage.core.LateBoundApplicationResolver;
import org.junit.Before;
import org.junit.Test;

public class JerseyApplicationBaseBinderTests {

    private ServiceLocator serviceLocator;

    @Before
    public void setUpServiceLocator() {
        serviceLocator = ServiceLocatorUtilities.bind(new JerseyApplicationBaseBinder());
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
    public void resourceConfigInstanceIsBoundToResourceConfigAsSingleton() {
        ResourceConfig resourceConfig1 = serviceLocator.getService(ResourceConfig.class);
        ResourceConfig resourceConfig2 = serviceLocator.getService(ResourceConfig.class);

        assertThat(resourceConfig1)
                .isNotNull()
                .isEqualTo(resourceConfig2);
    }
}

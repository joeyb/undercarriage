package org.joeyb.undercarriage.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class LateBoundApplicationResolverTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public Application<ConfigSection> application;

    @Mock
    public TestApplication<ConfigSection> testApplication;

    @Test
    public void applicationThrowsIfHasNotBeenSet() {
        ApplicationResolver applicationResolver = new LateBoundApplicationResolver();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(applicationResolver::application);
    }

    @Test
    public void applicationOfTypeThrowsIfHasNotBeenSet() {
        ApplicationResolver applicationResolver = new LateBoundApplicationResolver();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.application(Application.class));
    }

    @Test
    public void optionalApplicationOfTypeThrowsIfHasNotBeenSet() {
        ApplicationResolver applicationResolver = new LateBoundApplicationResolver();

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.optionalApplication(Application.class));
    }

    @Test
    public void applicationReturnsSuppliedApplication() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(application);

        assertThat(applicationResolver.application()).isEqualTo(application);
    }

    @Test
    public void applicationOfTypeReturnsSuppliedApplication() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(application);

        assertThat(applicationResolver.application(Application.class)).isEqualTo(application);
    }

    @Test
    public void applicationOfTypeReturnsSuppliedApplicationForMoreGeneralAppType() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(testApplication);

        assertThat(applicationResolver.application(Application.class)).isEqualTo(testApplication);
    }

    @Test
    public void applicationOfTypeThrowsForMoreSpecificAppType() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(application);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> applicationResolver.application(TestApplication.class));
    }

    @Test
    public void optionalApplicationOfTypeReturnsSuppliedApplication() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(application);

        assertThat(applicationResolver.optionalApplication(Application.class))
                .containsSame(application);
    }

    @Test
    public void optionalApplicationOfTypeReturnsSuppliedApplicationForMoreGeneralAppType() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(testApplication);

        assertThat(applicationResolver.optionalApplication(Application.class))
                .containsSame(testApplication);
    }

    @Test
    public void optionalApplicationOfTypeReturnsEmptyForMoreSpecificAppType() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();
        applicationResolver.setApplication(application);

        assertThat(applicationResolver.optionalApplication(TestApplication.class))
                .isNotPresent();
    }

    @Test
    public void setApplicationThrowsIfApplicationIsNull() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> applicationResolver.setApplication(null));
    }

    @Test
    public void setApplicationThrowsIfApplicationIsSetTwice() {
        LateBoundApplicationResolver applicationResolver = new LateBoundApplicationResolver();

        applicationResolver.setApplication(application);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.setApplication(application));
    }

    private interface TestApplication<ConfigT extends ConfigSection> extends Application<ConfigT> {

    }
}

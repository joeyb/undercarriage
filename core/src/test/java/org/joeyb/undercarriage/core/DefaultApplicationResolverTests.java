package org.joeyb.undercarriage.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class DefaultApplicationResolverTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public Application<ConfigSection> application;

    @Mock
    public TestApplication<ConfigSection> testApplication;

    @Test
    public void constructorThrowsIfSupplierIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new DefaultApplicationResolver(null));
    }

    @Test
    public void applicationThrowsIfSuppliedApplicationIsNull() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(() -> null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(applicationResolver::application);
    }

    @Test
    public void applicationOfTypeThrowsIfSuppliedApplicationIsNull() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(() -> null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.application(Application.class));
    }

    @Test
    public void optionalApplicationOfTypeThrowsIfSuppliedApplicationIsNull() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(() -> null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.optionalApplication(Application.class));
    }

    @Test
    public void applicationReturnsSuppliedApplication() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> application);

        assertThat(applicationResolver.application()).isEqualTo(application);
    }

    @Test
    public void applicationOfTypeReturnsSuppliedApplication() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> application);

        assertThat(applicationResolver.application(Application.class)).isEqualTo(application);
    }

    @Test
    public void applicationOfTypeReturnsSuppliedApplicationForMoreGeneralAppType() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> testApplication);

        assertThat(applicationResolver.application(Application.class)).isEqualTo(testApplication);
    }

    @Test
    public void applicationOfTypeThrowsForMoreSpecificAppType() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> application);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> applicationResolver.application(TestApplication.class));
    }

    @Test
    public void optionalApplicationOfTypeReturnsSuppliedApplication() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> application);

        assertThat(applicationResolver.optionalApplication(Application.class))
                .containsSame(application);
    }

    @Test
    public void optionalApplicationOfTypeReturnsSuppliedApplicationForMoreGeneralAppType() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> testApplication);

        assertThat(applicationResolver.optionalApplication(Application.class))
                .containsSame(testApplication);
    }

    @Test
    public void optionalApplicationOfTypeReturnsEmptyForMoreSpecificAppType() {
        ApplicationResolver applicationResolver = new DefaultApplicationResolver(
                () -> application);

        assertThat(applicationResolver.optionalApplication(TestApplication.class))
                .isNotPresent();
    }

    private interface TestApplication<ConfigT extends ConfigSection> extends Application<ConfigT> {

    }
}

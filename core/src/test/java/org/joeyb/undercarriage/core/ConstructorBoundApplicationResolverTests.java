package org.joeyb.undercarriage.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ConstructorBoundApplicationResolverTests {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public Application<ConfigSection> application;

    @Mock
    public TestApplication<ConfigSection> testApplication;

    @Test
    public void constructorThrowsIfSupplierIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ConstructorBoundApplicationResolver(null));
    }

    @Test
    public void applicationThrowsIfSuppliedApplicationIsNull() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(() -> null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(applicationResolver::application);
    }

    @Test
    public void applicationOfTypeThrowsIfSuppliedApplicationIsNull() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(() -> null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.application(Application.class));
    }

    @Test
    public void optionalApplicationOfTypeThrowsIfSuppliedApplicationIsNull() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(() -> null);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> applicationResolver.optionalApplication(Application.class));
    }

    @Test
    public void applicationReturnsSuppliedApplication() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> application);

        assertThat(applicationResolver.application()).isEqualTo(application);
    }

    @Test
    public void applicationOfTypeReturnsSuppliedApplication() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> application);

        assertThat(applicationResolver.application(Application.class)).isEqualTo(application);
    }

    @Test
    public void applicationOfTypeReturnsSuppliedApplicationForMoreGeneralAppType() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> testApplication);

        assertThat(applicationResolver.application(Application.class)).isEqualTo(testApplication);
    }

    @Test
    public void applicationOfTypeThrowsForMoreSpecificAppType() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> application);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> applicationResolver.application(TestApplication.class));
    }

    @Test
    public void optionalApplicationOfTypeReturnsSuppliedApplication() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> application);

        assertThat(applicationResolver.optionalApplication(Application.class))
                .containsSame(application);
    }

    @Test
    public void optionalApplicationOfTypeReturnsSuppliedApplicationForMoreGeneralAppType() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> testApplication);

        assertThat(applicationResolver.optionalApplication(Application.class))
                .containsSame(testApplication);
    }

    @Test
    public void optionalApplicationOfTypeReturnsEmptyForMoreSpecificAppType() {
        ApplicationResolver applicationResolver = new ConstructorBoundApplicationResolver(
                () -> application);

        assertThat(applicationResolver.optionalApplication(TestApplication.class))
                .isNotPresent();
    }

    private interface TestApplication<ConfigT extends ConfigSection> extends Application<ConfigT> {

    }
}

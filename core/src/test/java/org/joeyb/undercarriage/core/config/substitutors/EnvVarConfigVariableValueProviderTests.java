package org.joeyb.undercarriage.core.config.substitutors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.UUID;

public class EnvVarConfigVariableValueProviderTests {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void prefixIsCorrect() {
        EnvVarConfigVariableValueProvider provider = new EnvVarConfigVariableValueProvider();

        assertThat(provider.variableNamePrefix()).isEqualTo(EnvVarConfigVariableValueProvider.VARIABLE_NAME_PREFIX);
    }

    @Test
    public void valueIsFetchedFromEnvVar() {
        final String name = UUID.randomUUID().toString();
        final String value = UUID.randomUUID().toString();

        environmentVariables.set(name, value);

        EnvVarConfigVariableValueProvider provider = new EnvVarConfigVariableValueProvider();

        assertThat(provider.value(name)).hasValue(value);
    }
}

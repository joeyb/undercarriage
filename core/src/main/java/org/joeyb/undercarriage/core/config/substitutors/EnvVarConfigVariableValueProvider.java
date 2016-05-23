package org.joeyb.undercarriage.core.config.substitutors;

import java.util.Optional;

/**
 * {@code EnvVarConfigVariableValueProvider} is an implementation of {@link ConfigVariableValueProvider} that pulls
 * config values from environment variables using {@link System#getenv(String)}.
 */
public class EnvVarConfigVariableValueProvider implements ConfigVariableValueProvider {

    public static final String VARIABLE_NAME_PREFIX = "env.";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> value(String variableName) {
        return Optional.ofNullable(System.getenv(variableName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String variableNamePrefix() {
        return VARIABLE_NAME_PREFIX;
    }
}

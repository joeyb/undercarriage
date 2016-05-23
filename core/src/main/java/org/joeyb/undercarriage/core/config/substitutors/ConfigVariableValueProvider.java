package org.joeyb.undercarriage.core.config.substitutors;

import java.util.Optional;

/**
 * {@code ConfigVariableValueProvider} defines the interface for config variable value providers.
 */
public interface ConfigVariableValueProvider {

    /**
     * Returns an {@link Optional} with the variable's value if it is known, otherwise returns an empty
     * {@link Optional}. The given {@code variableName} has already had the prefix stripped.
     *
     * @param variableName the variable name, without the provider's prefix
     */
    Optional<String> value(String variableName);

    /**
     * Returns the prefix used on variable names to match with this provider.
     */
    String variableNamePrefix();
}

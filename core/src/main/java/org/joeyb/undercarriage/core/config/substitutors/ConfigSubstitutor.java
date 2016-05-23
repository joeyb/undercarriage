package org.joeyb.undercarriage.core.config.substitutors;

/**
 * {@code ConfigSubstitutor} defines the interface for interpolating external values into the config.
 */
public interface ConfigSubstitutor {

    /**
     * Returns the config with all variables replaced with their external values.
     *
     * @param config the app's config, potentially containing variables
     */
    String substitute(String config);
}

package org.joeyb.undercarriage.core.config.substitutors;

/**
 * {@code NullConfigSubstitutor} is an implementation of {@link ConfigSubstitutor} that does nothing to the given config
 * and returns it as-is.
 */
public class NullConfigSubstitutor implements ConfigSubstitutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public String substitute(String config) {
        return config;
    }
}

package org.joeyb.undercarriage.core.config.substitutors;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.Optional;

import javax.inject.Inject;

/**
 * {@code DefaultConfigSubstitutor} is an implementation of {@link ConfigSubstitutor} that uses the given
 * {@link ConfigVariableValueProvider} instances to replace variable placeholders in configs.
 *
 * <p>A variable should be prefixed with {@code $&#123;} and suffixed with {@code &#125;}. Also, each provider defines
 * its variable name prefix. For example, if the provider prefix is {@code provider.} and the variable name is
 * {@code key}, then the full placeholder would be {@code $&#123;provider.key&#125;}.
 */
public class DefaultConfigSubstitutor implements ConfigSubstitutor {

    public static final char ESCAPE = '\\';
    public static final String VARIABLE_PREFIX = "${";
    public static final String VARIABLE_SUFFIX = "}";

    private final Iterable<ConfigVariableValueProvider> configVariableValueProviders;

    private final StrSubstitutor strSubstitutor;

    @Inject
    public DefaultConfigSubstitutor(Iterable<ConfigVariableValueProvider> configVariableValueProviders) {
        this.configVariableValueProviders = configVariableValueProviders;
        this.strSubstitutor = new StrSubstitutor(
                new StrLookup<Object>() {
                    @Override
                    public String lookup(String key) {
                        return value(key);
                    }
                },
                VARIABLE_PREFIX,
                VARIABLE_SUFFIX,
                ESCAPE);
    }

    /**
     * Returns the config with all variables that match one of the {@link ConfigVariableValueProvider}s replaced with
     * their external values.
     *
     * @param config the app's config, potentially containing variables
     */
    @Override
    public String substitute(String config) {
        return strSubstitutor.replace(config);
    }

    private String value(String variableNameWithPrefix) {
        for (ConfigVariableValueProvider provider : configVariableValueProviders) {
            // If the prefix does not match then we can go ahead and move on to the next provider.
            if (!variableNameWithPrefix.startsWith(provider.variableNamePrefix())) {
                continue;
            }

            Optional<String> maybeValue = provider.value(
                    variableNameWithPrefix.substring(provider.variableNamePrefix().length()));

            if (maybeValue.isPresent()) {
                return maybeValue.get();
            }
        }

        return null;
    }
}

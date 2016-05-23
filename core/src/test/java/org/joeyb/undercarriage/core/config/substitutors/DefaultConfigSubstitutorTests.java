package org.joeyb.undercarriage.core.config.substitutors;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DefaultConfigSubstitutorTests {

    @Test
    public void substituteShouldReturnStringWithoutVariablesUnchanged() {
        final String config = UUID.randomUUID().toString();

        TestConfigSubstitutor substitutor = new TestConfigSubstitutor(ImmutableMap.of(), "");

        assertThat(substitutor.substitute(config)).isEqualTo(config);
    }

    @Test
    public void substituteShouldReturnStringVariableWithDifferentPrefixUnchanged() {
        final String config = "${prefix1.key}";

        TestConfigSubstitutor substitutor = new TestConfigSubstitutor(
                ImmutableMap.of("key", UUID.randomUUID().toString()),
                "prefix2");

        assertThat(substitutor.substitute(config)).isEqualTo(config);
    }

    @Test
    public void substituteShouldReplaceValueWithoutNamePrefix() {
        final String config = "${key}";
        final String value = UUID.randomUUID().toString();

        TestConfigSubstitutor substitutor = new TestConfigSubstitutor(
                ImmutableMap.of("key", value),
                "");

        assertThat(substitutor.substitute(config)).isEqualTo(value);
    }

    @Test
    public void substituteShouldReplaceValueWithNamePrefix() {
        final String config = "${prefix.key}";
        final String value = UUID.randomUUID().toString();

        TestConfigSubstitutor substitutor = new TestConfigSubstitutor(
                ImmutableMap.of("key", value),
                "prefix.");

        assertThat(substitutor.substitute(config)).isEqualTo(value);
    }

    @Test
    public void substituteShouldSupportEscapedPrefix() {
        final String config = "\\${key}";
        final String value = UUID.randomUUID().toString();

        TestConfigSubstitutor substitutor = new TestConfigSubstitutor(
                ImmutableMap.of("key", value),
                "");

        assertThat(substitutor.substitute(config)).isEqualTo("${key}");
    }

    @Test
    public void substituteShouldSupportDefaultValue() {
        final String defaultValue = UUID.randomUUID().toString();
        final String config = "${key:-" + defaultValue + "}";

        TestConfigSubstitutor substitutor = new TestConfigSubstitutor(
                ImmutableMap.of(),
                "");

        assertThat(substitutor.substitute(config)).isEqualTo(defaultValue);
    }

    @Test
    public void substituteShouldChooseCorrectProvider() {
        final String config1 = "${provider1.key}";
        final String config2 = "${provider2.key}";

        final String value1 = UUID.randomUUID().toString();
        final String value2 = UUID.randomUUID().toString();

        TestMultiConfigSubstitutor substitutor = new TestMultiConfigSubstitutor(
                ImmutableMap.of("key", value1),
                "provider1.",
                ImmutableMap.of("key", value2),
                "provider2.");

        assertThat(substitutor.substitute(config1)).isEqualTo(value1);
        assertThat(substitutor.substitute(config2)).isEqualTo(value2);
    }

    private static class TestConfigSubstitutor extends DefaultConfigSubstitutor {

        public TestConfigSubstitutor(Map<String, String> values, String variableNamePrefix) {
            super(ImmutableList.of(new TestConfigVariableValueProvider(values, variableNamePrefix)));
        }
    }

    private static class TestMultiConfigSubstitutor extends DefaultConfigSubstitutor {

        public TestMultiConfigSubstitutor(
                Map<String, String> values1,
                String variableNamePrefix1,
                Map<String, String> values2,
                String variableNamePrefix2) {

            super(ImmutableList.of(
                    new TestConfigVariableValueProvider(values1, variableNamePrefix1),
                    new TestConfigVariableValueProvider(values2, variableNamePrefix2)));
        }
    }

    private static class TestConfigVariableValueProvider implements ConfigVariableValueProvider {

        private final Map<String, String> values;
        private final String variableNamePrefix;

        public TestConfigVariableValueProvider(Map<String, String> values, String variableNamePrefix) {
            this.values = values;
            this.variableNamePrefix = variableNamePrefix;
        }

        @Override
        public Optional<String> value(String variableName) {
            return Optional.ofNullable(values.get(variableName));
        }

        @Override
        public String variableNamePrefix() {
            return variableNamePrefix;
        }
    }
}

package org.joeyb.undercarriage.config.yaml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;

import org.immutables.value.Value;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.config.substitutors.ConfigSubstitutor;
import org.joeyb.undercarriage.core.config.substitutors.NullConfigSubstitutor;
import org.junit.Test;

import java.util.UUID;

public class YamlConfigContextTests {

    @Test
    public void configSuccessfullyReadsGivenSimpleYamlConfig() {
        final String value = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(
                ImmutableList.of("value: " + value));

        YamlConfigContext<ValueConfig> yamlConfigContext =
                new YamlConfigContext<ValueConfig>(new NullConfigSubstitutor(), yamlConfigReader) { };

        ValueConfig config = yamlConfigContext.config();

        assertThat(config).isNotNull();
        assertThat(config.value()).isEqualTo(value);
    }

    @Test
    public void configIgnoresEmptyConfig() {
        final String value = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(
                ImmutableList.of(
                        "value: " + value,
                        ""));

        YamlConfigContext<ValueConfig> yamlConfigContext =
                new YamlConfigContext<ValueConfig>(new NullConfigSubstitutor(), yamlConfigReader) { };

        ValueConfig config = yamlConfigContext.config();

        assertThat(config).isNotNull();
        assertThat(config.value()).isEqualTo(value);
    }

    @Test
    public void configSuccessfullyMergesOverlappingSimpleYamlConfigs() {
        final String value1 = UUID.randomUUID().toString();
        final String value2 = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(
                ImmutableList.of(
                        "value: " + value1,
                        "value: " + value2));

        YamlConfigContext<ValueConfig> yamlConfigContext =
                new YamlConfigContext<ValueConfig>(new NullConfigSubstitutor(), yamlConfigReader) { };

        ValueConfig config = yamlConfigContext.config();

        assertThat(config).isNotNull();
        assertThat(config.value()).isEqualTo(value2);
    }

    @Test
    public void configSuccessfullyMergesOverlappingComplexYamlConfigsValuePresentInFirstConfigMissingInSecond() {
        final String valueConfig1Value1 = UUID.randomUUID().toString();
        final String valueConfig1Value2 = UUID.randomUUID().toString();
        final String valueConfig2Value1 = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(ImmutableList.of(
                // Config #1
                "valueConfig1:\n"
                + "  value: " + valueConfig1Value1 + "\n"
                + "valueConfig2:\n"
                + "  value: " + valueConfig2Value1 + "\n",
                // Config #2
                "valueConfig1:\n"
                + "  value: " + valueConfig1Value2 + "\n"));

        YamlConfigContext<ContainerConfig> yamlConfigContext =
                new YamlConfigContext<ContainerConfig>(new NullConfigSubstitutor(), yamlConfigReader) { };

        ContainerConfig config = yamlConfigContext.config();

        assertThat(config).isNotNull();
        assertThat(config.valueConfig1().value()).isEqualTo(valueConfig1Value2);
        assertThat(config.valueConfig2().value()).isEqualTo(valueConfig2Value1);
    }

    @Test
    public void configSuccessfullyMergesOverlappingComplexYamlConfigsValueMissingInFirstConfigPresentInSecond() {
        final String valueConfig1Value1 = UUID.randomUUID().toString();
        final String valueConfig1Value2 = UUID.randomUUID().toString();
        final String valueConfig2Value2 = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(ImmutableList.of(
                // Config #1
                "valueConfig1:\n"
                + "  value: " + valueConfig1Value1 + "\n",
                // Config #2
                "valueConfig1:\n"
                + "  value: " + valueConfig1Value2 + "\n"
                + "valueConfig2:\n"
                + "  value: " + valueConfig2Value2 + "\n"));

        YamlConfigContext<ContainerConfig> yamlConfigContext =
                new YamlConfigContext<ContainerConfig>(new NullConfigSubstitutor(), yamlConfigReader) { };

        ContainerConfig config = yamlConfigContext.config();

        assertThat(config).isNotNull();
        assertThat(config.valueConfig1().value()).isEqualTo(valueConfig1Value2);
        assertThat(config.valueConfig2().value()).isEqualTo(valueConfig2Value2);
    }

    @Test
    public void configThrowsForYamlStructureMismatch() {
        final String valueConfig1Value1 = UUID.randomUUID().toString();
        final String valueConfig1Value2 = UUID.randomUUID().toString();
        final String valueConfig2Value2 = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(ImmutableList.of(
                // Config #1 - Invalid structure
                "valueConfig1: " + valueConfig1Value1 + "\n",
                // Config #2
                "valueConfig1:\n"
                + "  value: " + valueConfig1Value2 + "\n"
                + "valueConfig2:\n"
                + "  value: " + valueConfig2Value2 + "\n"));

        YamlConfigContext<ContainerConfig> yamlConfigContext =
                new YamlConfigContext<ContainerConfig>(new NullConfigSubstitutor(), yamlConfigReader) { };

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(yamlConfigContext::config)
                // Make sure the exception mentions the bad field name
                .matches(e -> e.getMessage().contains("valueConfig1"));
    }

    @Test
    public void configUsesConfigSubstitutor() {
        final String config = "value: " + UUID.randomUUID().toString();

        ConfigSubstitutor configSubstitutor = mock(ConfigSubstitutor.class);
        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(configSubstitutor.substitute(eq(config))).thenReturn(config);

        when(yamlConfigReader.readConfigs()).thenReturn(
                ImmutableList.of(config));

        YamlConfigContext<ValueConfig> yamlConfigContext =
                new YamlConfigContext<ValueConfig>(configSubstitutor, yamlConfigReader) { };

        yamlConfigContext.config();

        verify(configSubstitutor).substitute(eq(config));
    }

    @Value.Immutable
    @JsonDeserialize(as = ImmutableContainerConfig.class)
    interface ContainerConfig extends ConfigSection {

        ValueConfig valueConfig1();

        ValueConfig valueConfig2();
    }

    @Value.Immutable
    @JsonDeserialize(as = ImmutableValueConfig.class)
    interface ValueConfig extends ConfigSection {

        String value();
    }
}

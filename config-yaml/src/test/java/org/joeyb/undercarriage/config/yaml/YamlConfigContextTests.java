package org.joeyb.undercarriage.config.yaml;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;
import org.immutables.value.Value;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YamlConfigContextTests {

    @Test
    public void test() {
        final String value = UUID.randomUUID().toString();

        YamlConfigReader yamlConfigReader = mock(YamlConfigReader.class);

        when(yamlConfigReader.readConfigs()).thenReturn(
                ImmutableList.of("value: " + value));

        YamlConfigContext<ValueConfig> yamlConfigContext = new YamlConfigContext<ValueConfig>(yamlConfigReader) {
            @Override
            public Class<ValueConfig> configClass() {
                return ValueConfig.class;
            }
        };

        ValueConfig config = yamlConfigContext.config();

        assertThat(config).isNotNull();
        assertThat(config.value()).isEqualTo(value);
    }

    @Value.Immutable
    @JsonDeserialize(as = ImmutableValueConfig.class)
    interface ValueConfig extends ConfigSection {

        String value();
    }
}

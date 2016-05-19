package org.joeyb.undercarriage.config.yaml;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ClasspathYamlConfigReaderTests {

    @Test
    public void defaultConstructorProvidesDefaultConfigSources() {
        ClasspathYamlConfigReader yamlConfigReader = new ClasspathYamlConfigReader();

        assertThat(yamlConfigReader.configSources())
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    public void constructorWithConfigSourcesGeneratesCorrectPaths() {
        final String configPath = UUID.randomUUID().toString() + "/";
        final String configSource = UUID.randomUUID().toString() + ".yaml";

        ClasspathYamlConfigReader yamlConfigReader = new ClasspathYamlConfigReader(
                configPath,
                ImmutableList.of(configSource));

        Collection<String> configSources = yamlConfigReader.configSources();

        assertThat(configSources)
                .isNotNull()
                .hasSize(1);

        String actualConfigSource = configSources.iterator().next();

        assertThat(actualConfigSource)
                .isEqualTo(configPath + configSource);
    }

    @Test
    public void constructorWithConfigSourcesThrowsForNullConfigPath() {
        final String configSource = UUID.randomUUID().toString() + ".yaml";

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ClasspathYamlConfigReader(null, ImmutableList.of(configSource)));
    }

    @Test
    public void constructorWithConfigSourcesThrowsForNullConfigSources() {
        final String configPath = UUID.randomUUID().toString() + "/";

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new ClasspathYamlConfigReader(configPath, null));
    }

    @Test
    public void readConfigsReturnsExpectedContents() {
        final String configPath = "simple-contents-test/";
        final String configSource = "config.yaml";

        ClasspathYamlConfigReader yamlConfigReader = new ClasspathYamlConfigReader(
                configPath,
                ImmutableList.of(configSource));

        Collection<String> configs = yamlConfigReader.readConfigs();

        assertThat(configs)
                .isNotNull()
                .hasSize(1);

        String config = configs.iterator().next();

        assertThat(config)
                .isNotNull()
                .contains("config.yaml contents");
    }

    @Test
    public void readConfigReturnsConfigsInCorrectOrder() {
        final String configPath = "order-test/";
        final Collection<String> configSources = ImmutableList.of("3-first.yaml", "1-second.yaml", "2-third.yaml");

        ClasspathYamlConfigReader yamlConfigReader = new ClasspathYamlConfigReader(
                configPath,
                configSources);

        Collection<String> configs = yamlConfigReader.readConfigs();

        assertThat(configs).isNotNull();
        assertThat(configs.stream().map(String::trim))
                .containsExactly("3-first.yaml contents", "1-second.yaml contents", "2-third.yaml contents");
    }
}

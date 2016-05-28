package org.joeyb.undercarriage.config.yaml;

import static java.util.Objects.requireNonNull;
import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * {@code ClasspathYamlConfigReader} is an implementation of {@link YamlConfigReader} that reads the config files from
 * the classpath. It finds all config files at the paths in {@link #configSources()},
 */
public class ClasspathYamlConfigReader implements YamlConfigReader {

    private static final String DEFAULT_CONFIG_PATH = "undercarriage/";
    private static final Collection<String> DEFAULT_CONFIG_SOURCES = ImmutableList.of(
            "base.yaml",
            "plugin.yaml",
            "app.yaml",
            "test.yaml");

    private final Collection<String> configSources;

    @Inject
    public ClasspathYamlConfigReader() {
        this(DEFAULT_CONFIG_PATH, DEFAULT_CONFIG_SOURCES);
    }

    public ClasspathYamlConfigReader(String configPath, Collection<String> configSources) {
        requireNonNull(configPath);

        this.configSources = configSources.stream()
                .map(s -> configPath + s)
                .collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
    }

    /**
     * Returns the full classpath paths for each config source. Each path may match multiple files.
     */
    public Collection<String> configSources() {
        return configSources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> readConfigs() {
        return wrapChecked(
                () -> {
                    final List<String> configs = new LinkedList<>();

                    for (final String source : configSources()) {
                        final ClassLoader loader = MoreObjects.firstNonNull(
                                Thread.currentThread().getContextClassLoader(),
                                ClasspathYamlConfigReader.class.getClassLoader());

                        final Enumeration<URL> urls = loader.getResources(source);

                        while (urls.hasMoreElements()) {
                            configs.add(Resources.toString(urls.nextElement(), StandardCharsets.UTF_8));
                        }
                    }

                    return configs;
                },
                "Error reading config resources.");
    }
}

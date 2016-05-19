package org.joeyb.undercarriage.config.yaml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;

import java.util.Collection;
import java.util.Iterator;

import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

/**
 * {@code YamlConfigContext} is a {@link ConfigContext} implementation that reads the config from YAML files. Due to
 * type erasure, the class is abstract because it needs an implementation for the {@link #configClass()} method.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class YamlConfigContext<ConfigT extends ConfigSection> implements ConfigContext<ConfigT> {

    private final Supplier<ConfigT> config = Suppliers.memoize(this::readConfig);
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new Jdk8Module());

    private final YamlConfigReader yamlConfigReader;

    protected YamlConfigContext(YamlConfigReader yamlConfigReader) {
        this.yamlConfigReader = yamlConfigReader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConfigT config() {
        return config.get();
    }

    /**
     * Returns the {@link Class} for the app's config. This is unfortunately necessary due to type erasure. Jackson
     * needs the {@link Class} to do its deserialization.
     */
    public abstract Class<ConfigT> configClass();

    private static JsonNode mergeJsonNodes(JsonNode mainNode, JsonNode updateNode) {
        final Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {
            final String fieldName = fieldNames.next();
            final JsonNode jsonNode = mainNode.get(fieldName);

            if (jsonNode != null && jsonNode.isObject()) {
                mergeJsonNodes(jsonNode, updateNode.get(fieldName));
            } else if (mainNode instanceof ObjectNode) {
                final JsonNode value = updateNode.get(fieldName);

                ((ObjectNode) mainNode).set(fieldName, value);
            }
        }

        return mainNode;
    }

    private ConfigT readConfig() {
        final Collection<String> configs = yamlConfigReader.readConfigs();

        final JsonNode mergedJsonNode = configs.stream()
                .filter(s -> !Strings.isNullOrEmpty(s))
                .map(s -> wrapChecked(() -> objectMapper.readValue(s, JsonNode.class)))
                .reduce(YamlConfigContext::mergeJsonNodes)
                .orElse(objectMapper.createObjectNode());

        return wrapChecked(() -> objectMapper.readValue(new TreeTraversingParser(mergedJsonNode), configClass()));
    }
}

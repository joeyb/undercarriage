package org.joeyb.undercarriage.config.yaml;

import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Strings;

import net.jodah.typetools.TypeResolver;

import org.joeyb.undercarriage.core.config.ConfigContext;
import org.joeyb.undercarriage.core.config.ConfigSection;
import org.joeyb.undercarriage.core.config.substitutors.ConfigSubstitutor;
import org.joeyb.undercarriage.core.utils.Suppliers;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * {@code YamlConfigContext} is a {@link ConfigContext} implementation that reads the config from YAML files. Due to
 * type erasure, the class is abstract in order for the {@link #configClass()} method to work properly.
 *
 * @param <ConfigT> the app's config type
 */
public abstract class YamlConfigContext<ConfigT extends ConfigSection> implements ConfigContext<ConfigT> {

    private final Supplier<ConfigT> config = Suppliers.memoize(this::readConfig);
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new Jdk8Module());

    private final ConfigSubstitutor configSubstitutor;
    private final YamlConfigReader yamlConfigReader;

    protected YamlConfigContext(ConfigSubstitutor configSubstitutor, YamlConfigReader yamlConfigReader) {
        this.configSubstitutor = configSubstitutor;
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
     * Returns the {@link Class} for the app's config. {@link YamlConfigContext} must be {@code abstract} for this to
     * work properly due to type erasure.
     */
    @SuppressWarnings("unchecked")
    private Class<ConfigT> configClass() {
        return (Class<ConfigT>) TypeResolver.resolveRawArgument(YamlConfigContext.class, getClass());
    }

    /**
     * Merges two {@link JsonNode} instances and stores the merged result in the given {@code mainNode}. If the two
     * nodes share a common field, then the value from {@code updateNode} takes precedence.
     *
     * @param mainNode the initial node that will be mutated and returned
     * @param updateNode the overwriting node
     * @return mutated version of mainNode with values pulled from updateNode
     */
    private static JsonNode mergeJsonNodes(JsonNode mainNode, JsonNode updateNode) {
        final Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {
            final String fieldName = fieldNames.next();
            final JsonNode mainChildNode = mainNode.get(fieldName);
            final JsonNode updateChildNode = updateNode.get(fieldName);

            if (mainChildNode != null && mainChildNode.getNodeType() != updateChildNode.getNodeType()) {
                throw new IllegalStateException(
                        "YAML structure mismatch on merge. Tried to merge fields with different types for field "
                        + fieldName + ". That probably means one of your configs is structured incorrectly.");
            }

            if (mainChildNode != null && mainChildNode.isObject()) {
                mergeJsonNodes(mainChildNode, updateChildNode);
            } else {
                ((ObjectNode) mainNode).set(fieldName, updateChildNode);
            }
        }

        return mainNode;
    }

    private ConfigT readConfig() {
        final Collection<String> configs = yamlConfigReader.readConfigs();

        final JsonNode mergedJsonNode = configs.stream()
                .filter(s -> !Strings.isNullOrEmpty(s))
                .map(configSubstitutor::substitute)
                .map(s -> wrapChecked(() -> objectMapper.readValue(s, JsonNode.class)))
                .reduce(YamlConfigContext::mergeJsonNodes)
                .orElse(objectMapper.createObjectNode());

        return wrapChecked(() -> objectMapper.readValue(new TreeTraversingParser(mergedJsonNode), configClass()));
    }
}

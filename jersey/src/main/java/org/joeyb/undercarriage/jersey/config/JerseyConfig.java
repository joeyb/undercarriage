package org.joeyb.undercarriage.jersey.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * {@code JerseyConfig} defines the config settings needed for all Jersey-based applications.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableJerseyConfig.class)
@JsonSerialize(as = ImmutableJerseyConfig.class)
public interface JerseyConfig {

    /**
     * Returns Jersey server's base URI. If the port is specified as 0, then a random available port is chosen at
     * runtime.
     */
    @Value.Default
    default String baseUri() {
        return "http://localhost:3000";
    }

    /**
     * Returns {@code boolean} indicating whether or not the application should join the {@link Thread} used by the
     * underlying server. If {@code true}, the app will block until the server returns. If {@code false}, the app will
     * continue execution after starting the serer.
     */
    @Value.Default
    default boolean joinServerThread() {
        return false;
    }
}

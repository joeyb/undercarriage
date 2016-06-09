package org.joeyb.undercarriage.jersey.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;

public class JerseyConfigTests {

    @Test
    public void allFieldsHaveDefaultValues() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JerseyConfig jerseyConfig = objectMapper.readValue("{}", JerseyConfig.class);

        assertThat(jerseyConfig).isNotNull();
    }
}

package org.joeyb.undercarriage.spark.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;

public class SparkConfigTests {

    @Test
    public void allFieldsHaveDefaultValues() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SparkConfig sparkConfig = objectMapper.readValue("{}", SparkConfig.class);

        assertThat(sparkConfig).isNotNull();
    }
}

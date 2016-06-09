package org.joeyb.undercarriage.grpc.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;

public class GrpcConfigTests {

    @Test
    public void allFieldsHaveDefaultValues() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        GrpcConfig grpcConfig = objectMapper.readValue("{}", GrpcConfig.class);

        assertThat(grpcConfig).isNotNull();
    }
}

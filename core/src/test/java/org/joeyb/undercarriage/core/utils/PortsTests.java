package org.joeyb.undercarriage.core.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PortsTests {

    @Test
    public void availablePortReturnsNonZeroPortNumber() {
        int port = Ports.availablePort();

        assertThat(port).isNotEqualTo(0);
    }
}

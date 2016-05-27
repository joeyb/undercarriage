package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PortsTests {

    @Test
    public void availablePortReturnsNonZeroPortNumber() {
        int port = Ports.availablePort();

        assertThat(port).isNotEqualTo(0);
    }
}

package org.joeyb.undercarriage.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.joeyb.undercarriage.core.utils.UtilsTestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

public class PortsTests {

    @Test
    public void portsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Ports.class);
    }

    @Test
    public void availablePortReturnsNonZeroPortNumber() {
        int port = Ports.availablePort();

        assertThat(port).isNotEqualTo(0);
    }
}

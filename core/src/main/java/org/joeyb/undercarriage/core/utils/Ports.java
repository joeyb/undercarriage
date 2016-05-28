package org.joeyb.undercarriage.core.utils;

import static org.joeyb.undercarriage.core.utils.Exceptions.wrapChecked;

import java.net.ServerSocket;

/**
 * {@code Ports} provides static helper methods for working with network ports.
 */
public class Ports {

    /**
     * Returns a random available port.
     */
    public static int availablePort() {
        return wrapChecked(
                () -> {
                    try (ServerSocket socket = new ServerSocket(0)) {
                        return socket.getLocalPort();
                    }
                },
                "Failed to find an available port.");
    }

    private Ports() { }
}

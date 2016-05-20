package org.joeyb.undercarriage.core.utils;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * {@code Ports} provides static helper methods for working with network ports.
 */
public class Ports {

    /**
     * Returns a random available port.
     */
    public static int availablePort() {
        try {
            try (ServerSocket socket = new ServerSocket(0)) {
                return socket.getLocalPort();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to find an available port.", e);
        }
    }

    private Ports() { }
}

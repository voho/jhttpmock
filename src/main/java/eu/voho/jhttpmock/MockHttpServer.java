package eu.voho.jhttpmock;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Mock HTTP server.
 */
public interface MockHttpServer extends StubbingStart {
    default URI getLocalhostHttpsUri() {
        return getLocalhostHttpsUri("/");
    }

    default URI getLocalhostHttpUri() {
        return getLocalhostHttpUri("/");
    }

    default URI getLocalhostHttpsUri(final String pathStartingWithSlash) {
        try {
            return new URI("https", null, "localhost", getPort(), pathStartingWithSlash, null, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    default URI getLocalhostHttpUri(final String pathStartingWithSlash) {
        try {
            return new URI("http", null, "localhost", getPort(), pathStartingWithSlash, null, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the port which the server is running at.
     * @return port number
     */
    int getPort();

    /**
     * Resets the mock to the initial state (no behaviour, no interactions).
     */
    void reset();
}

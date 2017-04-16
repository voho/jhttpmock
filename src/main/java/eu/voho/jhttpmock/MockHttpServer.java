package eu.voho.jhttpmock;

/**
 * Mock HTTP server.
 */
public interface MockHttpServer extends StubbingStart {
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
